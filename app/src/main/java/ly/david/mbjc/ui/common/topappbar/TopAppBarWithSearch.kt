package ly.david.mbjc.ui.common.topappbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TopAppBarWithSearch(
    onBack: () -> Unit = {},
    openDrawer: (() -> Unit)? = null,
    title: String,
    subtitle: String = "",
    showSearchIcon: Boolean = true,
    dropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = null,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    tabsTitles: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {}
) {
    var isSearchAndFilterMode by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // TODO: expand out from the icon
    AnimatedVisibility(
        visible = isSearchAndFilterMode,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth }
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth }
        )
    ) {

        // TODO: when returning, focus is in front of search text
        //  most apps seems to not bring up the keyboard when returning
        LaunchedEffect(Unit) {
            // TODO: only do them when first clicking on search icon
            focusRequester.requestFocus()
        }

        BackHandler {
            isSearchAndFilterMode = false
            onSearchTextChange("")
        }

        Column {
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    IconButton(onClick = {
                        isSearchAndFilterMode = false
                        onSearchTextChange("")
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                placeholder = { Text("Search") },
                trailingIcon = {
                    if (searchText.isEmpty()) return@TextField
                    IconButton(onClick = {
                        onSearchTextChange("")
                        focusRequester.requestFocus()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search field.")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                value = searchText,
                onValueChange = {
                    onSearchTextChange(it)
                }
            )

            // TODO: Filters

            Divider()
        }
    }

    AnimatedVisibility(
        visible = !isSearchAndFilterMode,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth }
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth }
        )
    ) {
        ScrollableTopAppBar(
            onBack = onBack,
            openDrawer = openDrawer,
            title = title,
            subtitle = subtitle,
            mainAction = {
                if (showSearchIcon) {
                    IconButton(onClick = {
                        isSearchAndFilterMode = true
                    }) {
                        Icon(Icons.Default.Search, "")
                    }
                }
            },
            dropdownMenuItems = dropdownMenuItems,
            tabsTitles = tabsTitles,
            selectedTabIndex = selectedTabIndex,
            onSelectTabIndex = onSelectTabIndex
        )
    }
}
