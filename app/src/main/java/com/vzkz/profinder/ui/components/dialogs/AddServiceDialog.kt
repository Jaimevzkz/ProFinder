package com.vzkz.profinder.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vzkz.profinder.R
import com.vzkz.profinder.core.PROFESSIONALMODELFORTESTS
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.core.Constants.VALUENOTSET
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.components.MyColumn
import com.vzkz.profinder.ui.components.MyGenericTextField
import com.vzkz.profinder.ui.components.MySpacer
import com.vzkz.profinder.ui.theme.ProFinderTheme

@Composable
fun AddServiceDialog(
    modifier: Modifier = Modifier,
    owner : ActorModel,
    onDismiss: () -> Unit,
    onConfirm: (ServiceModel) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var category: Categories by remember { mutableStateOf(Categories.Household) }
    var description by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                if(name.isNotEmpty() && description.isNotEmpty()){
                    onConfirm(
                        ServiceModel(
                            sid = VALUENOTSET,
                            uid = VALUENOTSET,
                            name = name,
                            isActive = true,
                            category = category,
                            servDescription = description,
                            owner = owner,
                            price = 0.0 //todo develop functionality
                        )
                    )
                }
            }) {
                Text(text = stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = {
            Text(text = stringResource(R.string.add_service))
        },
        text = {
            MyColumn {
                val spacing = 20
                MyGenericTextField(
                    modifier = Modifier,
                    outlined = true,
                    hint = stringResource(R.string.name),
                    text = name,
                    onTextChanged = {
                        name = it
                    }
                )
                MySpacer(size = spacing)

                Box(modifier = Modifier) {
                    var expandedCategoryDropdownMenu by remember { mutableStateOf(false) }
                    Column {
                        MyGenericTextField(
                            modifier = Modifier,
                            outlined = true,
                            readOnly = true,
                            leadingIcon = {
                                IconButton(
                                    onClick = { expandedCategoryDropdownMenu = true }
                                ) {
                                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                                }
                            },
                            hint = stringResource(R.string.category),
                            text = category.name,
                            onTextChanged = { /*do nothing*/ }
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier,
                        expanded = expandedCategoryDropdownMenu,
                        onDismissRequest = { expandedCategoryDropdownMenu = false }) {
                        Categories.entries.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(it.name, style = MaterialTheme.typography.bodyMedium)
                                },
                                onClick = {
                                    category = it
                                    expandedCategoryDropdownMenu = false
                                },
                            )
                        }
                    }
                }

                MySpacer(size = spacing)
                MyGenericTextField(
                    modifier = Modifier,
                    outlined = true,
                    hint = stringResource(R.string.description),
                    text = description,
                    onTextChanged = {
                        description = it
                    }
                )
            }
        }
    )

}

@Composable
@Preview(showSystemUi = false)
private fun AddServiceDialogPreview() {
    ProFinderTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AddServiceDialog(
                onDismiss = {},
                onConfirm = {},
                owner = PROFESSIONALMODELFORTESTS
            )
        }
    }
}

/*
onDismissRequest: () -> Unit,
confirmButton: @Composable () -> Unit,
modifier: Modifier = Modifier,
dismissButton: @Composable (() -> Unit)? = null,
icon: @Composable (() -> Unit)? = null,
title: @Composable (() -> Unit)? = null,
text: @Composable (() -> Unit)? = null,
shape: Shape = AlertDialogDefaults.shape,
containerColor: Color = AlertDialogDefaults.containerColor,
iconContentColor: Color = AlertDialogDefaults.iconContentColor,
titleContentColor: Color = AlertDialogDefaults.titleContentColor,
textContentColor: Color = AlertDialogDefaults.textContentColor,
tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
properties: DialogProperties = DialogProperties()
 */