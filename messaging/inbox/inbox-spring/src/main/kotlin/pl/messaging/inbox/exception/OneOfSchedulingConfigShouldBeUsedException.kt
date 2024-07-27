package pl.messaging.inbox.exception

class OneOfSchedulingConfigShouldBeUsedException: RuntimeException("Only one of scheduling config value (fixedRate, fixedDelay, cron) should be defined")