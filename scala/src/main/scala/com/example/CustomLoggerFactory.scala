package com.example

import com.tersesystems.echopraxia.Field
import com.tersesystems.echopraxia.core._
import com.tersesystems.echopraxia.sapi._
import com.tersesystems.echopraxia.sapi.sourcecode.support._

object CustomLoggerFactory {
  private val FQCN: String = classOf[DefaultLoggerMethods[_]].getName
  private val fieldBuilder: CustomFieldBuilder = new CustomFieldBuilder

  def getLogger(name: String): CustomLogger = {
    val core = CoreLoggerFactory.getLogger(FQCN, name)
    new CustomLogger(core, fieldBuilder)
  }

  def getLogger(clazz: Class[_]): CustomLogger = {
    val core = CoreLoggerFactory.getLogger(FQCN, clazz.getName)
    new CustomLogger(core, fieldBuilder)
  }

  def getLogger: CustomLogger = {
    val core = CoreLoggerFactory.getLogger(FQCN, Caller.resolveClassName)
    new CustomLogger(core, fieldBuilder)
  }
}

final class CustomLogger(core: CoreLogger, fieldBuilder: CustomFieldBuilder)
  extends AbstractLoggerSupport(core, fieldBuilder) with DefaultLoggerMethods[CustomFieldBuilder] {

  @inline
  private def newLogger(coreLogger: CoreLogger): CustomLogger = new CustomLogger(coreLogger, fieldBuilder)

  @inline
  def withCondition(scalaCondition: Condition): CustomLogger = newLogger(core.withCondition(scalaCondition.asJava))

  @inline
  def withFields(f: CustomFieldBuilder => java.util.List[Field]): CustomLogger = {
    import scala.compat.java8.FunctionConverters._
    newLogger(core.withFields(f.asJava, fieldBuilder))
  }

  @inline
  def withThreadContext: CustomLogger = {
    import com.tersesystems.echopraxia.support.Utilities
    newLogger(core.withThreadContext(Utilities.threadContext()))
  }
}

final class CustomFieldBuilder extends FieldBuilder {
  import java.time.Instant

  implicit val instantToStringValue: ToValue[Instant] = ToValue(i => Field.Value.string(i.toString))

  def instant(name: String, instant: Instant): Field = keyValue(name, instant)
  def instant(tuple: (String, Instant)): Field = instant(tuple._1, tuple._2)

  implicit def mapToObjectValue[V: ToValue]: ToObjectValue[Map[String, V]] = (t: Map[String, V]) => {
    import scala.collection.JavaConverters._

    val fields: Seq[Field] = t.map {
      case (k, v) => keyValue(k, v.toString)
    }.toSeq
    Field.Value.`object`(fields.asJava)
  }
}



