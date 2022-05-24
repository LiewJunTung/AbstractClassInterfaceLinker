package com.liewjuntung.abstract_class_interface_linker.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class AbstractClassInterfaceLinkerProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AbstractClassInterfaceLinkerProcessor(environment.codeGenerator, environment.logger, environment.options)
    }
}