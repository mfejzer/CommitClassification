package pl.umk.bugclassification.scmparser

import org.apache.log4j.Level

import com.codahale.logula.Logging

trait LoggingConf {
  Logging.configure { log =>
    log.registerWithJMX = true

    log.level = Level.INFO
    //  log.loggers("") = Level.OFF

    log.console.enabled = true
    log.console.threshold = Level.INFO

    log.file.enabled = false
    log.file.filename = "/var/log/scmparser.log"
    log.file.maxSize = 10 * 1024 // KB
    log.file.retainedFiles = 5 // keep five old logs around

    log.syslog.enabled = false
  }
}