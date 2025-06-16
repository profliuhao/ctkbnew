package org.haoai.medixhub.ctkb;


import org.haoai.medixhub.ctkb.entity.Measurement;
import org.haoai.medixhub.ctkb.service.AllCriteriaService;
import org.haoai.medixhub.ctkb.service.CommonCriteriaStatsService;
import org.haoai.medixhub.ctkb.service.CriterionRankService;
import org.haoai.medixhub.ctkb.service.MeasurementService;
import org.haoai.medixhub.ctkb.utils.NumberManipulateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;
import java.util.*;

@SpringBootTest
public class TestCriteriaService {

    @Autowired
    private CommonCriteriaStatsService ccss;

    @Autowired
    private AllCriteriaService acs;

    @Autowired
    private MeasurementService ms;

    @Autowired
    private CriterionRankService crs;


    @Test
    public void testFindCriteriaPhase() {
        String conditionName = "4285271";
        Map<String, Integer> criteriaCountByPhase = ccss.getCriteriaCountByPhase(conditionName, 1);
        criteriaCountByPhase.forEach((u, v) ->
                System.out.println(u + " : " + v)
        );
    }


    @Test
    public void testIncCriteriaCount() {
        String criteriaId = "3038553";
        int count = acs.getInclusionCountByCriteriaId(criteriaId);
        System.out.println("inc count = " + count);
    }

    @Test
    public void testCriteriaRank() {
        String criteriaId = "3038553";
        int count = crs.getRankingByCriteriaId(criteriaId);
        System.out.println("criteria rank is " + count);

//        test a non existing criterion
        criteriaId = "30385539";
        count = crs.getRankingByCriteriaId(criteriaId);
        System.out.println("criteria rank is " + count);

    }


    @Test
    public void testCriteriaValues() {
        String criteriaId = "3038553";
        List<Measurement> values = ms.getAllMeasurementValuesByConceptId(criteriaId);
        Map<String,Object> map=new HashMap<String,Object>();
        float userMax = (float) -1.0;
        float userMin = (float)-1.0;

        if (values.size() > 10) {
            float maxval = -1;
            float minval = -1;
            DecimalFormat df = new DecimalFormat("#.##");
            if (userMax != -1.0 && userMin != -1.0) {
                System.out.println("user min " + userMin);
                System.out.println("user max " + userMax);
                maxval = (float) userMax;
                minval = (float) userMin;
            } else {
                ArrayList<Float> arrMin = new ArrayList<>();
                ArrayList<Float> arrMax = new ArrayList<>();
                for (Measurement val : values) {
                    arrMin.add(val.getMin());
                    arrMax.add(val.getMax());
                }

                Map<Float, Integer> mapMin = NumberManipulateUtil.countFreq(arrMin);
                Map<Float, Integer> mapMax = NumberManipulateUtil.countFreq(arrMax);

                if (mapMin.keySet().contains((float) -99999)) {
                    mapMin.remove((float) -99999);
                }
                if (mapMax.keySet().contains((float) 99999)) {
                    mapMax.remove((float) 99999);
                }

                float minValue = Collections.max(mapMin.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
                float maxValue = Collections.max(mapMax.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

                float initialMin = 0;
                float initialMax = maxValue;
                while (minValue >= maxValue) {
                    float prevMinVal = minValue;
                    while (prevMinVal <= minValue) {
                        if (mapMin.entrySet().isEmpty()) {
                            minValue = initialMin;
                            break;
                        } else {
                            mapMin.remove(minValue);
                            minValue = Collections.max(mapMin.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
                        }
                    }

                    float prevMaxVal = maxValue;
                    while (prevMaxVal >= maxValue) {
                        if (mapMax.entrySet().isEmpty()) {
                            maxValue = initialMax;
                            break;
                        } else {
                            mapMax.remove(maxValue);
                            maxValue = Collections.max(mapMax.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

                        }
                    }
                }
                System.out.println("default max: " + maxValue);
                System.out.println("default min: " + minValue);


                minValue = Float.valueOf(df.format(minValue));
                maxValue = Float.valueOf(df.format(maxValue));


                float rv = maxValue - minValue;
                minval = minValue - rv;
                minval = Math.max(0, minval);
                maxval = maxValue + rv;
            }

            int numberBins = 100;
            Map<Float, Integer> binsCounts = new TreeMap<>();
            float increment = (maxval - minval) / numberBins;

            DecimalFormat dff = new DecimalFormat("#.####");
            increment = Float.valueOf(dff.format(increment));

            for (int i = 0; i < numberBins; i++) {
                float bin = Float.valueOf(df.format(minval + i * increment));
                bin = Math.min(maxval, bin);
                binsCounts.put(bin, 0);
            }

            for (Float bin : binsCounts.keySet()) {
                for (Measurement val : values) {
                    if (val.getInclude() == 1) {
                        if (val.getMin() <= bin && val.getMax() >= bin) {
                            binsCounts.put(bin, binsCounts.get(bin) + 1);
                        }

                    } else {
                        if (val.getMin() > bin || val.getMax() < bin) {
                            binsCounts.put(bin, binsCounts.get(bin) + 1);
                        }
                    }
                }
            }
            map.put("valuedis", binsCounts);
            map.put("value_min", minval);
            map.put("value_max", maxval);
            map.put("bin_list", binsCounts.keySet());
            map.put("count_list", binsCounts.values());
        }

        map.forEach((k,v)->
        {
            System.out.println("k = " + k);
            System.out.println("v = " + v);
        });

    }
}