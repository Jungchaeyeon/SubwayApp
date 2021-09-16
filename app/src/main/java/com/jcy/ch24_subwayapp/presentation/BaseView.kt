package com.jcy.ch24_subwayapp.presentation

/**
    view는 presenter를 알고, presenter도 뷰를 안다
 **/
interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT
}