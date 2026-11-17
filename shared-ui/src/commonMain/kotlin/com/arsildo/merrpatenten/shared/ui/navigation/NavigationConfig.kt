package com.arsildo.merrpatenten.shared.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.Catalog
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.SignDetail
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Dashboard
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Disclaimer
import com.arsildo.merrpatenten.shared.feature.exam.navigation.Exam
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExamResultBottomSheet
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExitExamBottomSheet
import com.arsildo.merrpatenten.shared.feature.imagedetails.navigation.ZoomableImage
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.ClearMistakesBottomSheet
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.Mistakes
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.MistakesPractice
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.MistakesPracticeResultBottomSheet
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.LanguageSelection
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.Preferences
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.DeleteResultsBottomSheet
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.Statistics
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal val appSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Dashboard::class, Dashboard.serializer())
            subclass(Disclaimer::class, Disclaimer.serializer())
            subclass(Exam::class, Exam.serializer())
            subclass(ExamResultBottomSheet::class, ExamResultBottomSheet.serializer())
            subclass(ExitExamBottomSheet::class, ExitExamBottomSheet.serializer())
            subclass(ZoomableImage::class, ZoomableImage.serializer())
            subclass(Statistics::class, Statistics.serializer())
            subclass(DeleteResultsBottomSheet::class, DeleteResultsBottomSheet.serializer())
            subclass(Preferences::class, Preferences.serializer())
            subclass(LanguageSelection::class, LanguageSelection.serializer())
            subclass(Catalog::class, Catalog.serializer())
            subclass(SignDetail::class, SignDetail.serializer())
            subclass(Mistakes::class, Mistakes.serializer())
            subclass(ClearMistakesBottomSheet::class, ClearMistakesBottomSheet.serializer())
            subclass(MistakesPractice::class, MistakesPractice.serializer())
            subclass(MistakesPracticeResultBottomSheet::class, MistakesPracticeResultBottomSheet.serializer())
        }
    }
}
