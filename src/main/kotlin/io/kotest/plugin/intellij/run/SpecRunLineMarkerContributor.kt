package io.kotest.plugin.intellij.run

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import io.kotest.plugin.intellij.psi.getSpecEntryPoint
import io.kotest.plugin.intellij.psi.isTestFile
import io.kotest.plugin.intellij.testMode
import org.jetbrains.kotlin.psi.KtClassOrObject

/**
 * Returns a [RunLineMarkerContributor.Info] for a spec class.
 *
 * The entry point is the 'class' or 'object' keyword that is part of the spec's
 * definition in code. In psi terms, this is a leaf element whose element type is KTK, and context
 * element is [KtClassOrObject].
 */
class SpecRunLineMarkerContributor : RunLineMarkerContributor() {

   // icons list https://jetbrains.design/intellij/resources/icons_list/
   private val icon = AllIcons.RunConfigurations.TestState.Run_run

   override fun getInfo(element: PsiElement): Info? {
      when (element) {
         // the docs say to only run a line marker for a leaf
         is LeafPsiElement -> {
            if (!testMode && !element.containingFile.isTestFile()) return null
            val spec = element.getSpecEntryPoint()
            if (spec != null) {
               return Info(
                  icon,
                  com.intellij.util.Function<PsiElement, String> { "Run ${spec.fqName?.shortName()}" },
                  *ExecutorAction.getActions(1)
               )
            }
         }
      }
      return null
   }
}
