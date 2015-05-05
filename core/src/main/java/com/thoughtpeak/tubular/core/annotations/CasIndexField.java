package com.thoughtpeak.tubular.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * This annotation tells the CAS indexer what field/class variable type to use as a index value for
 * the type that is added to the CAS.
 * 
 * There can only be one annotation type for each class. The value of the variable that
 * this annotation points to is usually unique but can contain multiple of the same
 * value. The CAS when querying this index for the type will return multiple results if thats
 * the case
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CasIndexField {
	/**
	 * The type of index to use on this field
	 * @return
	 */
	String indexType();

}
