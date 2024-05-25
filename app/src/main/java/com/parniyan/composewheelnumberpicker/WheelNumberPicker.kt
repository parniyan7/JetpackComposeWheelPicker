package com.parniyan.composewheelnumberpicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 ** Created by Parniyan on 5/19/2024.
 **
 */


@Composable
fun BaseNumberPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    current: Int? = null,
    onValueChanged: (Int) -> Unit
) {

    // This function calls the BaseNumberPickerImpl function, passing the provided parameters.
    BaseNumberPickerImpl(
        modifier = modifier,
        current = current ?: 0,
        onTimeChanged = onValueChanged,
        items = items
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalSnapperApi::class)
@Composable
private fun BaseNumberPickerImpl(
    modifier: Modifier,
    current: Int,
    items: List<String>,
    onTimeChanged: (Int) -> Unit
) {
    // This line creates a list of numbers that will be displayed in the number picker, including some additional padding items.
    val numbers = remember { getNumbers(items) }

    // These lines create a LazyListState and a CoroutineScope, which are used to manage the scrolling behavior of the number picker.
    val numberListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isSetInProgress by remember { mutableStateOf(true) }

    // This line collects the state of whether the user is currently dragging the number picker.
    val isUserMoving by numberListState.interactionSource.collectIsDraggedAsState()

    // This line creates a SnapperLayoutInfo, which is used to calculate the animated scale and rotation of the number picker items.
    val numbersSnapInfo = rememberLazyListSnapperLayoutInfo(lazyListState = numberListState)

    // This LaunchedEffect block is used to scroll the number picker to the currently selected value.
    LaunchedEffect(current) {
        isSetInProgress = true
        scope.launch {
            if (current != 0)
                numberListState.animateScrollToItem((current).coerceAtLeast(0))
            delay(500)
        }.invokeOnCompletion {
            isSetInProgress = false
        }
    }

    // This LaunchedEffect block is used to call the onTimeChanged function whenever the user stops interacting with the number picker.
    LaunchedEffect(
        !numberListState.isScrollInProgress
    ) {
        if (!isSetInProgress && !isUserMoving) {
            onTimeChanged(
                (numberListState.firstVisibleItemIndex)
            )
        }
    }

    // This Box and LazyColumn are used to create the visual representation of the number picker.
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .requiredHeight(Height),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.matchParentSize(),
            state = numberListState,
            flingBehavior = rememberSnapFlingBehavior(
                lazyListState = numberListState
            ),
        ) {
            itemsIndexed(numbers) { index, item ->
                // This block calculates the animated scale and rotation of the number picker items based on their position in the list.
                val (scale, rotation) = calculateAnimatedScaleAndRotation(
                    lazyListState = numberListState,
                    snapperLayoutInfo = numbersSnapInfo,
                    index = index
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    // This function creates the visual representation of a single number picker item.
                    TimeText(
                        modifier = Modifier
                            .requiredHeight(TimeTextHeight)
                            .graphicsLayer {
                                this.scaleX = scale
                                this.scaleY = scale
                                this.rotationX = rotation
                            },
                        text = item,
                        color = Color.Gray
                    )
                }
            }
        }

        // This Box creates a semi-transparent background for the number picker.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .requiredHeight(TimeTextHeight)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Gray.copy(alpha = 0.1f)
                )
        )
    }
}

@Composable
private fun TimeText(
    // This function creates the visual representation of a single number picker item.
    modifier: Modifier,
    text: String,
    color: Color
) {
    // This Text component displays the text of the number picker item.
    Text(
        modifier = modifier
            .wrapContentSize()
            .height(TimeTextHeight),
        textAlign = TextAlign.Center,
        text = text,
        style = TextStyle.Default,
        color = color,
    )
}

private fun getNumbers(items: List<String>): List<String> {
    // This function creates a list of strings that will be displayed in the number picker, including some additional padding items.
    return mutableListOf<String>().apply {
        add("")
        add("")
        add("")
        add("")
        addAll(items)
        add("")
        add("")
        add("")
        add("")
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun calculateAnimatedScaleAndRotation(
    // This function calculates the animated scale and rotation of the number picker items based on their position in the list.
    lazyListState: LazyListState,
    snapperLayoutInfo: SnapperLayoutInfo,
    index: Int,
): Pair<Float, Float> {
    // This block calculates the distance of the current item from the center of the number picker.
    val distanceToIndexSnap = snapperLayoutInfo.distanceToIndexSnap(index).absoluteValue
    val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
    val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
    val singleViewPortHeight = viewPortHeight / 6
    val calculatedAlpha = 1.2f - (distanceToIndexSnap / singleViewPortHeight)

    // This block calculates the scale and rotation of the current item based on its distance from the center of the number picker.
    val maxScale = 1.2f
    val calculatedScale =
        maxScale - (maxScale - 1) * (1f - (1.2f - (distanceToIndexSnap / singleViewPortHeight)))

    val calculatedRotationX = -20f * (distanceToIndexSnap / singleViewPortHeight)

    // This block returns the calculated scale and rotation values.
    return if (calculatedRotationX.isNaN() || calculatedAlpha.isNaN())
        1f to 0f
    else
        calculatedScale to calculatedRotationX
}


// This function is a preview function that displays the number picker UI.
@Preview(showBackground = true)
@Composable
private fun PreviewTimePicker(
) {

    val items = remember { getItems() }
    var time by remember { mutableIntStateOf(0) }
    BaseNumberPicker(
        current = 0,
        onValueChanged = { time = it },
        items = items
    )
}


// This function creates a list of strings that will be displayed in the number picker.
fun getItems(): List<String> {
    return (0..60).toList().map { it.toString() }
}

private val Height = 360.dp
private val TimeTextHeight = 40.dp