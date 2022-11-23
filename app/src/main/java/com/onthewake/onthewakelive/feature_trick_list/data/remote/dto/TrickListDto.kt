package com.onthewake.onthewakelive.feature_trick_list.data.remote.dto

import com.onthewake.onthewakelive.feature_trick_list.data.local.TrickListEntity
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

data class TrickListDto(
    val spins: List<TrickItemDto>,
    val raileyTricks: List<TrickItemDto>,
    val backRollTricks: List<TrickItemDto>,
    val frontFlipTricks: List<TrickItemDto>,
    val frontRollTricks: List<TrickItemDto>,
    val tantrumTricks: List<TrickItemDto>,
    val whipTricks: List<TrickItemDto>,
    val grabs: List<TrickItemDto>,
    val rails: List<TrickItemDto>
)

fun TrickListDto.toTrickListEntity(): TrickListEntity = TrickListEntity(
    spins = spins.map { it.toTrickItem() },
    raileyTricks = raileyTricks.map { it.toTrickItem() },
    backRollTricks = backRollTricks.map { it.toTrickItem() },
    frontFlipTricks = frontFlipTricks.map { it.toTrickItem() },
    frontRollTricks = frontRollTricks.map { it.toTrickItem() },
    tantrumTricks = tantrumTricks.map { it.toTrickItem() },
    whipTricks = whipTricks.map { it.toTrickItem() },
    grabs = grabs.map { it.toTrickItem() },
    rails = rails.map { it.toTrickItem() }
)

fun TrickListDto.toTrickList(): TrickList = TrickList(
    spins = spins.map { it.toTrickItem() },
    raileyTricks = raileyTricks.map { it.toTrickItem() },
    backRollTricks = backRollTricks.map { it.toTrickItem() },
    frontFlipTricks = frontFlipTricks.map { it.toTrickItem() },
    frontRollTricks = frontRollTricks.map { it.toTrickItem() },
    tantrumTricks = tantrumTricks.map { it.toTrickItem() },
    whipTricks = whipTricks.map { it.toTrickItem() },
    grabs = grabs.map { it.toTrickItem() },
    rails = rails.map { it.toTrickItem() },
)
