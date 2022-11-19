package com.onthewake.onthewakelive.feature_trick_list.data.remote.response

import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList

data class TrickListResponse(
    val spins: List<TrickItemResponse>,
    val raileyTricks: List<TrickItemResponse>,
    val backRollTricks: List<TrickItemResponse>,
    val frontFlipTricks: List<TrickItemResponse>,
    val frontRollTricks: List<TrickItemResponse>,
    val tantrumTricks: List<TrickItemResponse>,
    val whipTricks: List<TrickItemResponse>,
    val grabs: List<TrickItemResponse>,
    val rails: List<TrickItemResponse>
)

fun TrickListResponse.toTrickList(): TrickList = TrickList(
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
