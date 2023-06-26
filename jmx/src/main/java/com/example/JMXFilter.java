package com.example;

import com.tersesystems.echopraxia.api.*;
import com.tersesystems.echopraxia.spi.*;
import com.tersesystems.jmxbuilder.DynamicBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.*;
import org.jetbrains.annotations.NotNull;

public class JMXFilter implements CoreLoggerFilter {
  private static final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

  private final ConcurrentHashMap<String, ConditionFlag> flagMap = new ConcurrentHashMap<>();

  @Override
  public CoreLogger apply(CoreLogger coreLogger) {
    ConditionFlag flag = registerFlag(coreLogger.getName());
    Condition c = (level, ctx) -> level.isGreaterOrEqual(Level.INFO) || flag.isEnabled();
    return coreLogger.withCondition(c);
  }

  private ConditionFlag registerFlag(String name) {
    ConditionFlag flag = flagMap.computeIfAbsent(name, n -> new ConditionFlag(n, false));
    try {
      ObjectName objectName = getObjectName(name);
      if (!mbeanServer.isRegistered(objectName)) {
        final DynamicMBean flagBean =
            DynamicBean.builder()
                .withSimpleAttribute(String.class, "name", flag::getName)
                .withSimpleAttribute(Boolean.TYPE, "enabled", flag::isEnabled, flag::setEnabled)
                .build();

        // If you use https://github.com/tersesystems/jmxmvc then you don't have to register
        // individual beans but that's another example app.
        mbeanServer.registerMBean(flagBean, objectName);
      }
    } catch (InstanceAlreadyExistsException e) {
      // do nothing, we don't care if it already exists.
      ;
    } catch (MalformedObjectNameException
        | NotCompliantMBeanException
        | MBeanRegistrationException e) {
      throw new RuntimeException(e);
    }
    return flag;
  }

  @NotNull
  private ObjectName getObjectName(String name) throws MalformedObjectNameException {
    return new ObjectName("conditions:type=ConditionFlag,name=" + name);
  }
}
