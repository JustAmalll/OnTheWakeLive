package com.onthewake.onthewakelive.feature_trick_list.data.remote.dto

import com.onthewake.onthewakelive.feature_trick_list.data.local.TrickListEntity
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

data class TrickListDto(
    val spins: List<TrickItem> = emptyList(),
    val raileyTricks: List<TrickItem> = emptyList(),
    val backRollTricks: List<TrickItem> = emptyList(),
    val frontFlipTricks: List<TrickItem> = emptyList(),
    val frontRollTricks: List<TrickItem> = emptyList(),
    val tantrumTricks: List<TrickItem> = emptyList(),
    val whipTricks: List<TrickItem> = emptyList(),
    val grabs: List<TrickItem> = emptyList(),
    val rails: List<TrickItem> = emptyList()
)

fun TrickListDto.toTrickListEntity(): TrickListEntity = TrickListEntity(
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

fun TrickListDto.toTrickList(): TrickList = TrickList(
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
