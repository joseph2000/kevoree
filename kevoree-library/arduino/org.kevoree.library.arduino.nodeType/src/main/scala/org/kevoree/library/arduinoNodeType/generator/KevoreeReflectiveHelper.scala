/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kevoree.library.arduinoNodeType.generator

import org.kevoree.TypeDefinition
import org.osgi.framework.BundleContext

trait KevoreeReflectiveHelper {

  def createStandaloneInstance(ct: TypeDefinition, bundleContext: BundleContext,nodeName:String): Object = {
    //CREATE NEW INSTANCE
    var clazzFactory: Class[_] = null
    val activatorClassName = ct.getFactoryBean.replaceAll("Factory", "Activator")
    if (bundleContext != null) {
      clazzFactory = bundleContext.getBundle.loadClass(activatorClassName)
    } else {
      clazzFactory = this.getClass.getClassLoader.loadClass(activatorClassName)
    }
    val activatorInstance = clazzFactory.newInstance

    val reflectiveInstanceActor = clazzFactory.getMethod("callFactory").invoke(activatorInstance)
    val clazzActor = reflectiveInstanceActor.getClass

    val reflectiveInstance = clazzActor.getMethods.find(method => {
      method.getName == "getKevoreeComponentType"
    }) match {
      case Some(method) => {
        method.invoke(reflectiveInstanceActor)
      }
      case None => reflectiveInstanceActor
    }

    //REFLEXIVE SET NODENAME
    reflectiveInstance.getClass.getMethods.find(method => method.getName == "setNodeName") match {
      case Some(method) => method.invoke(reflectiveInstance, nodeName)
      case None => println("NodeName not set !")
    }





    //val clazz = reflectiveInstance.getClass


    reflectiveInstance


    //CREATE INSTANCE DICTIONARY
    /*
    val dictionary: java.util.HashMap[String, String] = new java.util.HashMap[String, String]
    if (instance.getTypeDefinition.getDictionaryType != null) {
      if (instance.getTypeDefinition.getDictionaryType.getDefaultValues != null) {
        instance.getTypeDefinition.getDictionaryType.getDefaultValues.foreach {
          dv =>
          dictionary.put(dv.getAttribute.getName, dv.getValue)
        }
      }
    }

    if (instance.getDictionary != null) {
      instance.getDictionary.getValues.foreach {
        v =>
        dictionary.put(v.getAttribute.getName, v.getValue)
      }
    }
    //REFLEXIVE SET DICTIONARY
    clazz.getMethods.find(method => method.getName == "setDictionary") match {
      case Some(method)=> method.invoke(reflectiveInstance, dictionary)
      case None => println("Dictionary not set !")
    }

    //REFLEXIVE SET NAME
    clazz.getMethods.find(method => method.getName == "setName") match {
      case Some(method)=> method.invoke(reflectiveInstance, instance.asInstanceOf[NamedElement].getName)
      case None => println("Dictionary not set !")
    }
    //REFLEXIVE SET NODENAME
    clazz.getMethods.find(method => method.getName == "setNodeName") match {
      case Some(method)=> method.invoke(reflectiveInstance, nodeName)
      case None => println("Dictionary not set !")
    } */


  }

}