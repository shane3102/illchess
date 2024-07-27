package pl.messaging.inbox.exception

class NoneOfSchedulingValuesWasProvidedException :
    RuntimeException("One of scheduling values (fixedRate, fixedDelay, cron) have to be defined for inbox")