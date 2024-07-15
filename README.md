```markdown
# BaseNumberPicker

The `BaseNumberPicker` is a highly customizable and visually appealing number picker UI component built using Jetpack Compose. It provides a smooth and engaging user experience for selecting values from a list of items.

## Features

- Supports a list of strings as the items to be displayed in the number picker
- Allows setting the currently selected value
- Provides a callback function for when the selected value changes
- Includes an animated scale and rotation effect for the displayed items
- Includes a semi-transparent background for the number picker
- Supports scrolling and fling gestures for selecting values

## Usage

To use the `BaseNumberPicker` in your Jetpack Compose application, follow these steps:

1. Import the necessary classes:

```kotlin
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
```

2. Call the `BaseNumberPicker` Composable function, passing in the required parameters:

```kotlin
@Composable
fun MyScreen() {
    var currentValue by rememberSaveable { mutableStateOf(0) }

    BaseNumberPicker(
        modifier = Modifier.fillMaxWidth(),
        items = (0..60).map { it.toString() },
        current = currentValue,
        onValueChanged = { currentValue = it }
    )
}
```

Here's a breakdown of the parameters:

- `modifier`: The modifier to be applied to the `BaseNumberPicker`.
- `items`: The list of strings to be displayed in the number picker.
- `current`: The currently selected value (can be `null`).
- `onValueChanged`: The callback function that is called when the selected value changes.

## Customization

The `BaseNumberPicker` can be further customized by modifying the `BaseNumberPickerImpl` Composable function, which is the private implementation of the number picker. You can adjust the following aspects:

- Adjust the size and appearance of the number picker items by modifying the `TimeText` Composable function.
- Customize the background of the number picker by modifying the `Box` with the `background` modifier.
- Tweak the animation parameters, such as the scale and rotation, by modifying the `calculateAnimatedScaleAndRotation` function.

## Dependencies

The `BaseNumberPicker` Composable function relies on the following Jetpack Compose libraries:

- `androidx.compose.foundation:foundation`
- `androidx.compose.ui:ui`
- `androidx.compose.runtime:runtime`
- `com.google.accompanist:accompanist-snapper`

Make sure to include these dependencies in your project's build.gradle file.

## Preview

You can preview the `BaseNumberPicker` Composable function by using the provided `PreviewTimePicker` Composable function.

```kotlin
@Preview(showBackground = true)
@Composable
fun PreviewTimePicker() {
    val items = getItems()
    var time by remember { mutableIntStateOf(0) }
    BaseNumberPicker(
        current = 0,
        onValueChanged = { time = it },
        items = items
    )
}
```

This will display the number picker UI in the Android Studio preview window.

## Contribution

If you have any suggestions, bug reports, or feature requests, feel free to open an issue or submit a pull request on the [GitHub repository](https://github.com/parniyan7/JetpackComposeWheelPicker).
