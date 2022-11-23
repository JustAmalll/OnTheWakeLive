package com.onthewake.onthewakelive.feature_trick_list.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickItem
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

@Entity
data class TrickListEntity(
    @PrimaryKey val id: Int? = null,
    val spins: List<TrickItem>,
    val raileyTricks: List<TrickItem>,
    val backRollTricks: List<TrickItem>,
    val frontFlipTricks: List<TrickItem>,
    val frontRollTricks: List<TrickItem>,
    val tantrumTricks: List<TrickItem>,
    val whipTricks: List<TrickItem>,
    val grabs: List<TrickItem>,
    val rails: List<TrickItem>
)

fun TrickListEntity.toTrickList(): TrickList = TrickList(
    spins = spins,
    raileyTricks = raileyTricks,
    backRollTricks = backRollTricks,
    frontFlipTricks = frontFlipTricks,
    frontRollTricks = frontRollTricks,
    tantrumTricks = tantrumTricks,
    whipTricks = whipTricks,
    grabs = grabs,
    rails = rails
)