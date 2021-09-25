package com.jcy.ch24_subwayapp.presentation.stations

import com.jcy.ch24_subwayapp.domain.Station
import com.jcy.ch24_subwayapp.presentation.BasePresenter
import com.jcy.ch24_subwayapp.presentation.BaseView

interface StationsContract {
    interface View: BaseView<Presenter>{
        fun showLoadingIndicatier()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }
    interface Presenter: BasePresenter{ //역을 검색했을 때 필터링 된 결과
        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)
    }
}