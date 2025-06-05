package zelimkhan.magomadov.contactsrevive.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import zelimkhan.magomadov.contactsrevive.R
import zelimkhan.magomadov.contactsrevive.feature.backup.navigation.BackupRoute
import zelimkhan.magomadov.contactsrevive.feature.importing.navigation.ImportingRoute
import zelimkhan.magomadov.contactsrevive.feature.instruction.navigation.InstructionRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    IMPORTING(
        selectedIcon = R.drawable.ic_import_active,
        unselectedIcon = R.drawable.ic_import,
        iconTextId = R.string.feature_import,
        titleTextId = R.string.feature_import,
        route = ImportingRoute::class
    ),

    BACKUP(
        selectedIcon = R.drawable.ic_backup_active,
        unselectedIcon = R.drawable.ic_backup,
        iconTextId = R.string.feature_backup,
        titleTextId = R.string.feature_backup,
        route = BackupRoute::class
    ),

    INSTRUCTION(
        selectedIcon = R.drawable.ic_instruction_active,
        unselectedIcon = R.drawable.ic_instruction,
        iconTextId = R.string.feature_instruction,
        titleTextId = R.string.feature_instruction,
        route = InstructionRoute::class
    )
}