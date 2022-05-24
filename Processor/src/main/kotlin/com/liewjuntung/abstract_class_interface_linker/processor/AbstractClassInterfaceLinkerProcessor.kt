package com.liewjuntung.abstract_class_interface_linker.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.liewjuntung.abstract_class_interface_linker.annotation.AbstractClassInterfaceLinker
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.OutputStream

class AbstractClassInterfaceLinkerProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger,
    options: Map<String, String>
) :
    SymbolProcessor {
    @OptIn(KotlinPoetKspPreview::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val qualifiedName = AbstractClassInterfaceLinker::class.qualifiedName
        if (qualifiedName != null) {
            val symbols = resolver.getSymbolsWithAnnotation(qualifiedName)


            val ret = symbols.filter { !it.validate() }.toList()
            symbols
                .filter { it is KSClassDeclaration && it.validate() }
                .forEach { it.accept(TestVisitor(), Unit) }
            return ret
        }
        return emptyList()
    }

    @KotlinPoetKspPreview
    inner class TestVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val interfaceFunctionMap = hashMapOf<String, KSFunctionDeclaration>()

            classDeclaration.getDeclaredFunctions().forEach { func ->
                if (func.isAbstract) {
                    interfaceFunctionMap[func.simpleName.asString()] = func
                }
            }

            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val className = "${classDeclaration.simpleName.asString()}Handler"
            val classBuilder = TypeSpec.classBuilder(className)



            classDeclaration.annotations.forEach { annotation ->
                annotation.arguments.forEach { valueArgument ->
                    val abstractClassType = valueArgument.value as KSType?
                    classBuilder.superclass(abstractClassType!!.toClassName())
//                    file.appendText("package $packageName\n\n")
//                    file.appendText("class $className(private val iInterface: ${classDeclaration.qualifiedName?.asString()}): ${abstractClassType?.declaration?.qualifiedName?.asString()}() {\n")
                    classBuilder.primaryConstructor(
                        FunSpec.constructorBuilder()

                            .addParameter("iInterface", classDeclaration.toClassName())
                            .build()
                    ).addProperty(
                        PropertySpec.builder("iInterface", classDeclaration.toClassName())
                            .initializer("iInterface")
                            .addModifiers(KModifier.PRIVATE)
                            .build()
                    )
                    abstractClassType
                        .declaration
                        .closestClassDeclaration()
                        ?.getAllFunctions()
                        ?.forEach { func ->
                            if (func.isAbstract) {
                                val functionName = func.simpleName.asString()
                                val paramsWithTypes = StringBuilder()
                                val params = StringBuilder()
                                val docString = func.docString

                                func.parameters.forEach { parameter ->
                                    params.append(parameter.name?.asString()?.lowercase())
                                    paramsWithTypes.append(parameter.name?.asString()?.lowercase())
                                    paramsWithTypes.append(": ")
                                    paramsWithTypes.append(parameter.type?.resolve()?.declaration?.qualifiedName?.asString())
                                    paramsWithTypes.append(",")
                                    params.append(",")
                                }
                                val functionReturn =
                                    if (func.returnType == null || func.returnType!!.resolve().declaration.qualifiedName!!.asString() == "kotlin.Unit") "" else ": " + func.returnType!!.resolve().declaration.qualifiedName!!.asString()

                                classBuilder.addFunction(
                                    FunSpec.builder(functionName)

                                        .addModifiers(KModifier.OVERRIDE)
                                        .addParameters(func.parameters.map { x ->
                                            ParameterSpec.builder(
                                                x.name!!.asString(),
                                                x.type.toTypeName()
                                            ).build()
                                        })
                                        .apply {
                                            if (docString != null) {
                                                addKdoc(docString)
                                            }
                                            val returnString = if (functionReturn.isEmpty()){
                                                ""
                                            } else {
                                                "return "
                                            }
                                            if (interfaceFunctionMap.containsKey(functionName)) {
                                                addStatement("${returnString}iInterface.${functionName}(${
                                                    func.parameters.joinToString(
                                                        ","
                                                    ) { x -> x.name!!.asString() }
                                                })", ).build()
                                            } else {
                                                addStatement("TODO(\"Not Implemented\")")
                                            }
                                        }
                                        .returns(
                                            func.returnType!!
                                                .resolve().declaration.closestClassDeclaration()!!
                                                .toClassName()
                                        )
                                        .build()
                                )
//                                    file.appendText("\t/**     $docString\t**/\n")

//                                file.appendText("    override fun $functionName(${paramsWithTypes})${functionReturn} {\n")
//                                if (interfaceFunctionMap.contains(functionName)) {
//                                    if (functionReturn.isEmpty()) {
//                                        file.appendText("\t\tiInterface.${functionName}($params)\n")
//                                    } else {
//                                        file.appendText("\t\treturn iInterface.${functionName}($params)\n")
//                                    }
//                                } else {
//                                    file.appendText("\t\tTODO(\"NOT IMPLEMENTED\")\n")
//                                }
//                                file.appendText("    }\n\n")

                            }
                        }
//                    file.appendText("}\n")
//                    file.close()
                }
            }

            FileSpec.builder(packageName, className)
                .addType(classBuilder.build())
                .build()
                .writeTo(codeGenerator, dependencies = Dependencies(true))
        }
    }
}


fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}