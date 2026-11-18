package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

private val Categories = listOf("B", "C", "D")

@Composable
fun CategoryTabs(selectedCategory: String, onCategorySelected: (category: String) -> Unit, modifier: Modifier = Modifier) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        Categories.forEachIndexed { index, catKey ->
            SegmentedButton(
                selected = selectedCategory == catKey,
                onClick = { onCategorySelected(catKey) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = Categories.size),
            ) {
                Text(
                    text = catKey,
                    fontWeight = if (selectedCategory == catKey) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}
