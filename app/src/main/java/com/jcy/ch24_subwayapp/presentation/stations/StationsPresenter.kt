package com.jcy.ch24_subwayapp.presentation.stations

import com.jcy.ch24_subwayapp.data.repository.StationRepository
import com.jcy.ch24_subwayapp.domain.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StationsPresenter(
    private val view: StationsContract.View,
    private val stationRepository: StationRepository
) : StationsContract.Presenter{

    override val scope: CoroutineScope = MainScope()

    private val queryString: MutableStateFlow<String> = MutableStateFlow("")
    private val stations: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    init {
        observeStations()
    }
    override fun filterStations(query: String) {
        scope.launch {
            queryString.emit(query)
        }
    }

    override fun toggleStationFavorite(station: Station) {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorited = !station.isFavorited))
        }
    }

    override fun onViewCreated() {
        scope.launch {
            view.showStations(stations.value)
            stationRepository.refreshStations()
        }
    }

    override fun onDestroyView() {}

    private fun observeStations(){
        stationRepository
            .stations
            .combine(queryString){stations, query ->
                if(query.isBlank()){
                    stations
                }else{
                    stations.filter { it.name.contains(query) }
                }
            }
            .onStart { view.showLoadingIndicatier() }
            .onEach {
                if(it.isNotEmpty()){
                    view.hideLoadingIndicator()
                }
                stations.value = it
                view.showStations(it)
            }
            .catch {
                it.printStackTrace()
                view.hideLoadingIndicator()
            }
            .launchIn(scope)
    }

}