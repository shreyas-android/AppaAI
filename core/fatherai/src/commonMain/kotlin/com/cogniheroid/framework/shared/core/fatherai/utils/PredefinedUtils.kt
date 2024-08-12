package com.cogniheroid.framework.shared.core.fatherai.utils

import com.cogniheroid.framework.shared.core.fatherai.data.enum.GeminiAIModel
import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.shared.core.fatherai.data.enum.ModelReturnType
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo

object PredefinedUtils {


    private fun videoTranscript() = FatherAIInfo(1L, "Video transcript", GeminiAIModel.GEMINI_PRO, MediaType.VIDEO, "Generate transcript",true,
        ModelReturnType.TEXT, "Analyze the video and give the exact transcript with timestamps")

    private fun audioTranscript() = FatherAIInfo(1L, "Audio transcript", GeminiAIModel.GEMINI_PRO, MediaType.AUDIO,
        "Generate transcript",true,
        ModelReturnType.TEXT, "Analyze the audio and give the exact transcript with timestamps")


    private fun storyWriter() =  FatherAIInfo(1L, "Story writer", GeminiAIModel.GEMINI_PRO, MediaType.NONE,
        "Generate story",true,
        ModelReturnType.TEXT, "Create a story with all genre combined")

    private fun textFromImage() =  FatherAIInfo(1L, "Text extractor", GeminiAIModel.GEMINI_PRO, MediaType.IMAGE,
        "Extract text",true,
        ModelReturnType.TEXT, "Analyze the image and get the text present in the image. Especially main text or text which will get the more attention")


    private fun nutrition() =  FatherAIInfo(1L, "Nutri chef", GeminiAIModel.GEMINI_PRO, MediaType.IMAGE,
        "Get nutrients",true,
        ModelReturnType.TEXT, nutrientPrompt())

    private fun nutrientPrompt() : String {
        return """
          You are an expert in nutritionist where you need to accurately see the food items from the image , identify the food in the image accurately 
          and calculate the quantity of the food in grams or user friendly,  total calories, proteins, vitamins, minerals, carbohydrates, fats etc 
          and what are nutrient data available please provide it. Your data should be more accurate. It should match the value in internet. Also provide the details of every 
          food items with calories intake as below format.  Please follow the below format for generated text
          
          List of food Items present in image
          
          Item 1
          Item 2
          Item 3
          
          
          
       
          ITEM 1(Bold) quantity
          
          1. no of calories -
          2. no of protein -
          3. no of Carbohydrate -
          4. no of fats -
          5. no of vitamins -
          6. no of minerals -
          
          ITEM 2 (Bold) quantity
          
           1. no of calories -
          2. no of protein -
          3. no of Carbohydrate -
          4. no of fats -
          5. no of vitamins -
          6. no of minerals -
            
          ---
          ---
        """.trimIndent()

    }

    private fun foodRecipe() = FatherAIInfo(1L, "Food recipe", GeminiAIModel.GEMINI_PRO, MediaType.IMAGE,
        "Get recipe",true,
        ModelReturnType.TEXT, foodRecipePrompt())

    private fun foodRecipePrompt() : String {
        return """
          You are an expert in nutritionist where you need to accurately see the food items from the image if exists  , identify the food in the image accurately 
          if exists and give the recipe, ingredients, perfect quantity of ingredients and step by step procedure to do that food. Your data should be more accurate. It should match the value in internet. Also
           provide the output as below format. Please follow the below format for generated text
          
          Item 1 name(Bold)
      
          Ingredients : 
          
          1.  
          2.  
          ---
          
          Procedure : 
          
          Step 1 - 
          Step 2 -
          Step 3 - 
          ----
          
          If another food exist
          
          Item 2 name(Bold) 
      
          Ingredients : 
          
          1.  
          2.  
          ---
          
          Procedure : 
          
          Step 1 - 
          Step 2 -
          Step 3 - 
          ----
          
        """.trimIndent()

    }

    private fun equationInsights() = FatherAIInfo(1L, "Equation Recognizer",
        GeminiAIModel.GEMINI_PRO, MediaType.IMAGE,
        "Recognize equation",true,
        ModelReturnType.TEXT, equationPrompt())

    private fun equationPrompt() : String {
        return """
             You are an expert in equation analyzer where you need to accurately see the equation from the image , identify the equation in the image accurately 
          and  explain the equations, what is the name of the equation, founder and founder year. Your data should be more accurate. It should match the value in internet.
           Also provide the details of every equation as below format.  Please follow the below format for generated text
           
           Equation 1 
           
           
           Equation name - 
           
           Equation founder -
                      
           Equation founded year -
           
           Description of equation - 
           
           Example with real world example - 
           
          
          ---
       """.trimIndent()
    }

    fun getPredefinedList() = listOf(videoTranscript(), audioTranscript(), storyWriter(), textFromImage(), nutrition(), foodRecipe(), equationInsights())


}