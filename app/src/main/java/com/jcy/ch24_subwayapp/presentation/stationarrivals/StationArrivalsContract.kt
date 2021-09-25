package com.jcy.ch24_subwayapp.presentation.stationarrivals

import com.jcy.ch24_subwayapp.domain.ArrivalInformation
import com.jcy.ch24_subwayapp.presentation.BasePresenter
import com.jcy.ch24_subwayapp.presentation.BaseView

interface StationArrivalsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showErrorDescription(message: String)

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)
    }

    interface Presenter : BasePresenter {

        fun fetchStationArrivals()

        fun toggleStationFavorite()
    }
}