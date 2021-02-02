import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.classic.filter.ThresholdFilter
import net.logstash.logback.appender.LogstashTcpSocketAppender
import net.logstash.logback.composite.loggingevent.LoggingEventJsonProviders
import net.logstash.logback.encoder.LogstashEncoder
import org.springframework.boot.logging.logback.ColorConverter

statusListener(NopStatusListener)
//statusListener(OnConsoleStatusListener)

def env = System.getenv()
String appName = env['APPNAME'] ?: 'app'
String springProfile = env['SPRING_PROFILES_ACTIVE'] ?: 'profile'
String logFilePath = env['LOGS'] ?: 'logs'

String logFileName = "${logFilePath}/${appName}-${springProfile}.log"
String hibernateFileName = "${logFilePath}/${appName}-${springProfile}-hibernate.log"
String flywayFileName = "${logFilePath}/${appName}-${springProfile}-flyway.log"
String camelFileName = "${logFilePath}/${appName}-${springProfile}-camel.log"
String errorFileName = "${logFilePath}/${appName}-${springProfile}-error.log"
String logArchiveFileName = "${logFilePath}/archive/${appName}-${springProfile}"
String hibernateArchiveFileName = "${logFilePath}/archive/${appName}-hibernate"
String flywayArchiveFileName = "${logFilePath}/archive/${appName}-flyway"
String camelArchiveFileName = "${logFilePath}/archive/${appName}-camel"
String errorArchiveFileName = "${logFilePath}/archive/${appName}-error"

//<configuration scan="true">
//</configuration>
//<configuration debug="true">
//</configuration>

conversionRule("clr", ColorConverter)

appender("fileAppender", RollingFileAppender) {
    file = logFileName
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        totalSizeCap = '10MB'
        maxFileSize = '1MB'
        maxHistory = 10
        fileNamePattern = "${logArchiveFileName}-%d{yyyy-MM-dd}-%i.log.gz"
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
}

//appender("TEXT_FILE", RollingFileAppender) {
//    file = "${LOGS:-logs}/${APPNAME:-app-test}.log"
//    append = true
//    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
//        fileNamePattern = "${LOGS:-logs}/archive/${APPNAME:-app-test}.%d{yyyy-MM-dd}.%i.log.gz"
//        maxFileSize = "10MB"
//        maxHistory = 14
//        totalSizeCap = "1GB"
//    }
//    encoder(PatternLayoutEncoder) {
//        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
//    }
//}

appender("hibernateFileAppender", RollingFileAppender) {
    file = hibernateFileName
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        totalSizeCap = '10MB'
        maxFileSize = '1MB'
        maxHistory = 10
        fileNamePattern = "${hibernateArchiveFileName}-%d{yyyy-MM-dd}-%i.log.gz"
    }

    //TODO: should I comment this out
    filter(LevelFilter) {
        level = TRACE
    }
}

appender("flywayFileAppender", RollingFileAppender) {
    file = flywayFileName
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        totalSizeCap = '10MB'
        maxFileSize = '1MB'
        maxHistory = 10
        fileNamePattern = "${flywayArchiveFileName}-%d{yyyy-MM-dd}-%i.log.gz"
    }
}

appender("camelFileAppender", RollingFileAppender) {
    file = camelFileName
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        totalSizeCap = '10MB'
        maxFileSize = '1MB'
        maxHistory = 10
        fileNamePattern = "${camelArchiveFileName}-%d{yyyy-MM-dd}-%i.log.gz"
    }
}

appender("errorFileAppender", RollingFileAppender) {
    file = errorFileName
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        totalSizeCap = '10MB'
        maxFileSize = '1MB'
        maxHistory = 10
        fileNamePattern = "${errorArchiveFileName}-%d{yyyy-MM-dd}-%i.log.gz"
    }

    filter(ThresholdFilter) {
        level = ERROR
    }
}

appender("consoleAppender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%clr(%d{yyyy-MM-dd HH:mm:ss}){blue} %clr([%thread]){green} - %clr(%-5level){yellow} %clr(%-36.36logger{36}){cyan} - %clr(%msg){magenta}%n'
    }
}

appender("logstashAppender", LogstashTcpSocketAppender) {
    remoteHost = 'hornsup'
    port = 4560
    encoder(LogstashEncoder) {
        providers(LoggingEventJsonProviders) {
        }
    }
    //keepAliveDuration = "5 minutes"
}

logger('org.hibernate', INFO, ['fileAppender', 'hibernateFileAppender', 'logstashAppender'], false)
logger('org.flywaydb', INFO, ['fileAppender', 'flywayFileAppender', 'logstashAppender'])
logger('org.apache.camel', INFO, ['consoleAppender', 'fileAppender', 'camelFileAppender', 'logstashAppender'])

root(INFO, ['consoleAppender', 'fileAppender', 'errorFileAppender', 'logstashAppender'])
