package pl.umk.bugclassification.scmparser
import com.codahale.logula.Logging
import org.apache.log4j.Level

trait TestLoggingConf {
  Logging.configure { log =>
    log.registerWithJMX = false
    log.level = Level.INFO
    
    log.console.enabled = false
    log.console.threshold = Level.WARN
    
    log.file.enabled = false
    log.syslog.enabled = false
  }
}