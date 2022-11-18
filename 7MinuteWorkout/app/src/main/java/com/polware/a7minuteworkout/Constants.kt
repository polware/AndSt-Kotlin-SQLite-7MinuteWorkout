package com.polware.a7minuteworkout

import com.polware.a7minuteworkout.model.ExercisesModel

class Constants {

    companion object {

        fun defaultExerciseList(): ArrayList<ExercisesModel> {
            val exerciseList = ArrayList<ExercisesModel>()

            val jumpingJacks = ExercisesModel(1, "JumpingJacks", R.drawable.jumping_jacks,
                false, false)
            exerciseList.add(jumpingJacks)

            val wallSit = ExercisesModel(2, "Wall Sit", R.drawable.wall_sit,
                false, false)
            exerciseList.add(wallSit)

            val pushUp = ExercisesModel(3, "Push Up", R.drawable.push_up,
                false, false)
            exerciseList.add(pushUp)

            val abdominalCrunch = ExercisesModel(4, "Abdominal Crunch", R.drawable.abdominal_crunch,
                false, false)
            exerciseList.add(abdominalCrunch)

            val stepUpOnChair = ExercisesModel(5, "Step Up Onto Chair", R.drawable.step_up_onto_chair,
                false, false)
            exerciseList.add(stepUpOnChair)

            val squat = ExercisesModel(6, "Squat", R.drawable.squat_exercise,
                false, false)
            exerciseList.add(squat)

            val tricepsDip = ExercisesModel(7, "Triceps Dip", R.drawable.tricep_dips_exercise,
                false, false)
            exerciseList.add(tricepsDip)

            val plank = ExercisesModel(8, "Plank", R.drawable.plank_exercise,
                false, false)
            exerciseList.add(plank)

            val highKnees = ExercisesModel(9, "High Knees In Place", R.drawable.high_knees,
                false, false)
            exerciseList.add(highKnees)

            val lunges = ExercisesModel(10, "Lunges", R.drawable.lunges,
                false, false)
            exerciseList.add(lunges)

            val pushUpRotating = ExercisesModel(11, "Push Up Rotating", R.drawable.push_up_rotating,
                false, false)
            exerciseList.add(pushUpRotating)

            val sidePlank = ExercisesModel(12, "Side Plank", R.drawable.side_plank,
                false, false)
            exerciseList.add(sidePlank)

            return exerciseList
        }
    }

}