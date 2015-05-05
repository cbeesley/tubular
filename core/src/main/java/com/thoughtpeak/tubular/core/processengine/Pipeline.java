package com.thoughtpeak.tubular.core.processengine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;




import org.apache.log4j.Logger;

import com.thoughtpeak.tubular.core.annotations.AnalysisComponent;
import com.thoughtpeak.tubular.core.annotations.Initialize;
import com.thoughtpeak.tubular.core.cas.impl.BaseCASImpl;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
/**
 * Main pipeline class that contains all process analyzers
 * 
 * @author chrisbeesley
 *
 */
public class Pipeline {
	
	protected Logger log = Logger.getLogger(Pipeline.class);
	
	// if adding new properties, be sure to update createNewCopy method
	
	private final String pipelineName;
	
	private final Set<CoreAnnotationProcessor> pipeline;
	
	private final static String initalize_anno_classpath = "com.thoughtpeak.tubular.core.annotations.Initialize";
	
	public static class Assemble {
		protected Logger log = Logger.getLogger(Assemble.class);
		// Required
		private final String pipelineName;
		
		
		
		//Optional
		private final Set<CoreAnnotationProcessor> pipeline = new LinkedHashSet<CoreAnnotationProcessor>();
		
		public Assemble(String pipelineName){
			
			this.pipelineName = pipelineName;
			
		}
		
		public Assemble addAnalyzer(CoreAnnotationProcessor ae){
			pipeline.add(ae);
			return this;
		}
		/**
		 * Add additional processors via a supplied list
		 * 
		 * @param list
		 * @return
		 */
		public Assemble addAllAnalyzers(Collection<? extends CoreAnnotationProcessor> list){
			pipeline.addAll(list);
			
			return this;
		}
		
		public Pipeline create() {
			if(pipeline.isEmpty()){
				throw new IllegalArgumentException("In order to execute a pipeline, you need to set at least one processor engine");
			}
			// now look thru each annotator and see which java annotations need to be processed
			for (CoreAnnotationProcessor eachEngine : pipeline) {

				Class<?> inst = eachEngine.getClass();
				if(inst.getAnnotations().length < 1){
					// Error? no annotation marker present
				}
				for(Annotation classAnnotation : inst.getAnnotations()){
					String annoName = classAnnotation.annotationType().getName();
					// if its a analysis component, check for deps
					if(annoName.equals(AnalysisComponent.class.getName())){
						// load the dependancy list
						System.out.println(annoName);
						// check all dependancies are loaded into the pipeline, if they are not present, then
						// load them automatically
						
						// sort into pipeline
					}
				}

				Field[] fields = inst.getDeclaredFields();
				// Method level scan
				Method[] methods = inst.getMethods();
				for (Method method : methods) {
					method.setAccessible(true);
					Annotation[] anno = method.getAnnotations();
					for (Annotation eachAnno : anno) {
						String annoName = eachAnno.annotationType().getName();
						if(annoName.equals(Initialize.class.getName())){
							invokeInitialization(method,eachEngine);
						}
							
					}
						
				}
				for (Field eachField : fields) {
					eachField.setAccessible(true);
					Annotation[] anno = eachField.getAnnotations();
					for (Annotation eachAnno : anno) {
						System.out.println(eachAnno.annotationType());
					}

				}

			}
			return new Pipeline(this);
		}
		/**
		 * Invokes the "init" method of each Analysis engine
		 * @param methodToInvoke
		 * @param engine
		 */
		private void invokeInitialization(Method methodToInvoke,CoreAnnotationProcessor engine){
			try {
				methodToInvoke.invoke(engine, null);
			} catch (IllegalArgumentException e) {
				log.error("The initialization method in the analysis engine,"+ engine.getClass().getName() +" must have a no-argument parameter such as init()");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				log.error("The initialization method in the analysis engine,"+ engine.getClass().getName() +" must be public");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Pipeline(Assemble assembly){
		pipelineName = assembly.pipelineName;
		pipeline = assembly.pipeline;
	}
	
	/**
	 * Execute a single analysis operation usually for a single document
	 * where all the annotations are preserved.
	 * 
	 * @param text - Document to analyze
	 * returns a bin with all the derived annotations
	 */
	public CommonAnalysisStructure executePipeline(String text){
		
		log.debug("Executing Pipeline id #" + this.hashCode());
		
		CommonAnalysisStructure cas = BaseCASImpl.createInstance(text);
		
		for(CoreAnnotationProcessor eachAe : pipeline){
			eachAe.process(cas);
			
		}
		
		return cas;
	}
	
	public String getPipelineName(){
		return pipelineName;
	}
	
	/**
	 * Creates a new copy of this pipeline using the set properties
	 * in this instance
	 * 
	 * @return new pipeline based on the settings
	 */
	public Pipeline createNewCopy(){
		List<CoreAnnotationProcessor> newInstanceList = new ArrayList<CoreAnnotationProcessor>();
		
		// create new instances of each processor
		for(CoreAnnotationProcessor eachAe : pipeline){
			try {
				newInstanceList.add(eachAe.getClass().newInstance());
			} catch (InstantiationException e) {
				log.error("Error while trying to create a new instance of " +eachAe.getClass().getName() , e);
			} catch (IllegalAccessException e) {
				log.error("Error while trying to create a new instance of " +eachAe.getClass().getName(), e);
			}
		}
		
		Pipeline newPipeline = new Pipeline.Assemble(this.pipelineName)
		.addAllAnalyzers(newInstanceList)
		.create();
		
		return newPipeline;
	}
	

}
