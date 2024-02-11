package com.vzkz.profinder.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.vzkz.profinder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGenericTextField(
    modifier: Modifier,
    hint: String,
    text: String,
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = TextFieldDefaults.outlinedShape,
    outlined: Boolean = true
) {
    if (outlined){
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = { onTextChanged(it) },
            label = {
                Text(
                    text = hint,
                    fontSize = 16.sp
                )
            },
            keyboardOptions = KeyboardOptions.Default,
            singleLine = true,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            shape = shape,
            colors = if (readOnly) TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ) else TextFieldDefaults.outlinedTextFieldColors()
        )
    } else{
        TextField(
            modifier = modifier,
            value = text,
            onValueChange = { onTextChanged(it) },
            label = {
                Text(
                    text = hint,
                    fontSize = 16.sp
                )
            },
            keyboardOptions = KeyboardOptions.Default,
            singleLine = true,
            shape = shape,
            readOnly = readOnly,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            colors = if (readOnly) TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ) else TextFieldDefaults.outlinedTextFieldColors()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEmailTextField(
    modifier: Modifier,
    text: String,
    onTextChanged: (String) -> Unit,

    ) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onTextChanged(it) },
        label = {
            Text(
                text = stringResource(R.string.email),
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPasswordTextField(
    modifier: Modifier,
    text: String,
    hint: String,
    onTextChanged: (String) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val img = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onTextChanged(it) },
        label = {
            Text(
                text = hint,
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        singleLine = true,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = img, contentDescription = null)
            }
        }
    )
}
