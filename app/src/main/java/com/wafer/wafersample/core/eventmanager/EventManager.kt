package com.wafer.wafersample.core.eventmanager

import java.util.*

object EventManager {

    private val eventObserverMap = HashMap<Class<out Event>, MutableSet<Observer>>()

    fun registerObserver(eventClass: Class<out Event>, observer: Observer) {
        synchronized(eventObserverMap) {
            if (!eventObserverMap.containsKey(eventClass)) {
                val observers = Collections.newSetFromMap(WeakHashMap<Observer, Boolean>())
                observers.add(observer)
                eventObserverMap[eventClass] = observers
            } else {
                val observerManager = eventObserverMap[eventClass]
                observerManager?.add(observer)
            }
        }
    }

    fun unregisterObserver(eventClass: Class<out Event>, observer: Observer) {
        synchronized(eventObserverMap) {
            val observers = eventObserverMap[eventClass]
            observers?.remove(observer)
        }
    }

    fun sendEvent(event: Event) {
        val observers = eventObserverMap[event.javaClass]
        observers?.let {
            for (observer in it) {
                observer.onEvent(event)
            }
        }
    }
}