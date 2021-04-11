package dev.kxxcn.woozoora.common.base

class NotificationProvider {

    interface Factory {

        operator fun invoke(data: Map<String, String>)
    }
}
