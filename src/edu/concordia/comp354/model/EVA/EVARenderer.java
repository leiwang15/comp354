package edu.concordia.comp354.model.EVA;

/**
 * Created by joao on 15-08-10.
 */
public interface EVARenderer {

	void populateEVA(EarnedValueAnalysis eva);

	void selectEVADate(String dateStr);
}
