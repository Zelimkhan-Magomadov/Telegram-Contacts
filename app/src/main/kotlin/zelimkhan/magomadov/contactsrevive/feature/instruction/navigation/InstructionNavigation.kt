package zelimkhan.magomadov.contactsrevive.feature.instruction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import zelimkhan.magomadov.contactsrevive.feature.instruction.InstructionScreen

@Serializable
data object InstructionRoute

fun NavController.navigateToInstruction(navOptions: NavOptions) {
    return navigate(route = InstructionRoute, navOptions = navOptions)
}

fun NavGraphBuilder.instructionScreen() {
    composable<InstructionRoute> {
        InstructionScreen()
    }
}