package com.mlkang.albertsonsacromine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mlkang.albertsonsacromine.ui.theme.AlbertsonsAcromineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlbertsonsAcromineTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AcromineScreen()
                }
            }
        }
    }
}

@Composable
fun AcromineScreen(
    modifier: Modifier = Modifier,
    viewModel: AcromineViewModel = viewModel()
) {
    Column(modifier = modifier) {
        val acronymState by viewModel.acronymState.collectAsStateWithLifecycle()
        TextField(
            value = acronymState,
            onValueChange = { viewModel.updateAcronymInput(it) },
            placeholder = { Text("Enter Acronym") },
            modifier = Modifier.fillMaxWidth()
        )
        val longFormsResult by viewModel.longFormsState.collectAsStateWithLifecycle()
        (longFormsResult as? NetworkResult.Success)?.let { longFormsSuccess ->
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(longFormsSuccess.value) { longForm ->
                    Text(text = longForm.text, modifier = Modifier.padding(8.dp))
                }
            }
        }
        (longFormsResult as? NetworkResult.Loading)?.let {
            Text(text = "Loading...", modifier = Modifier.padding(8.dp))
        }
        (longFormsResult as? NetworkResult.Failure)?.let { failure ->
            Text(text = "Error: ${failure.error.message}", modifier = Modifier.padding(8.dp)) // TODO: Show user friendly error message
        }
    }
}