package com.liewjuntung.workload

import com.liewjuntung.abstract_class_interface_linker.annotation.AbstractClassInterfaceLinker
import com.soon.work.Something


@AbstractClassInterfaceLinker(FooAbstractClass::class)
interface FooInterface {
    fun doThis()
    fun doThat()
    fun doThingsReturn(): String
    fun doNothing(): String
}

@AbstractClassInterfaceLinker(BarAbstractClass::class)
interface BarInterface {
    /** Permissions */
    fun checkCameraPermission();

    fun mobileDeviceGetInfo(uuid: String);

    fun mobileDeviceInit(uuid: String, videoMode: Boolean);

    fun mobileDevicePreviewStart(uuid: String);

    fun mobileDevicePreviewStop(uuid: String);

    fun mobileDeviceHighResCapture(uuid: String);

    fun isGood(flagx: Boolean): Boolean

    fun doSomethingElse(doSomething: Something)
}


class FooClientService(private val fooInterface: FooInterface) : FooAbstractClass() {
    override fun doThis() {
        fooInterface.doThis()
    }

    override fun doThat() {
        fooInterface.doThat()
    }

    override fun doThisAlso() {
        TODO("Not yet implemented")
    }

    override fun doThingsReturn(): String {
        TODO("Not yet implemented")
    }
}