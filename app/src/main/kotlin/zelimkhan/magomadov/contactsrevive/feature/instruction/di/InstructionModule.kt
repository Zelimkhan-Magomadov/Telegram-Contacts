package zelimkhan.magomadov.contactsrevive.feature.instruction.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zelimkhan.magomadov.contactsrevive.feature.instruction.InstructionViewModel

val instructionModule = module {
    viewModelOf(::InstructionViewModel)
}