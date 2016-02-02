package com.visiontech.yummysmile.ui.presenter;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.MealsController;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;
import com.visiontech.yummysmile.ui.subscriber.ResultListener;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class MainPresenter extends BasePresenter {
    private final MainView mainView;
    private final MealsController mealsController;

    public MainPresenter(MainView mainView, YummySmileApplication application) {
        super(application);

        this.mainView = mainView;
        mealsController = application.getApiClientComponent().getMealsController();
    }

    public void fetchMeals() {
        mainView.showProgress(true);
        mealsController.getMeals(new ResultListener<MealsControllerImpl.MealsResponse>() {
            @Override
            public void onResult(MealsControllerImpl.MealsResponse result) {
                mainView.showProgress(false);
                if (result.isSuccess()) {
                    MealsDTO mealsDTO = result.getPayload();
                    if (mealsDTO != null && !mealsDTO.getMeals().isEmpty()) {
                        mainView.mealsItems(mealsDTO);
                    } else {
                        //TODO show some empty view here instead a toast.
                        mainView.showMessage("All good, but no meals");
                    }
                } else {
                    //FIXME find the final copy.
                    //TODO Do we have to show other view? like some text on the layout?
                    mainView.showMessage(getContext().getString(R.string.general_error, result.getError().getMessage()));
                }
            }
        });
    }
}
