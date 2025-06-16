package org.haoai.medixhub.ctkb.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.haoai.medixhub.ctkb.entity.Measurement;
import org.haoai.medixhub.ctkb.service.MeasurementService;
import org.haoai.medixhub.ctkb.utils.NumberManipulateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author haoliu
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/measurement")
@CrossOrigin
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

//    @GetMapping("/concept_id/{concept_id}")
//    public List<Measurement> searchMeasurementByConceptID(@PathVariable(value = "concept_id") String concept_id){
//        return measurementService.getByConceptID(concept_id);
//    }

    @GetMapping("/page/concept_id/{concept_id}")
    public IPage<Measurement> searchMeasurementByConceptIDPage(@PathVariable(value = "concept_id") String concept_id) {
        return measurementService.getByConceptIDPage(concept_id);
    }

    @GetMapping("/page/concept_id/{concept_id}/{current_page}/{size}")
    public IPage<Measurement> searchMeasurementByConceptIDPageWithSize(@PathVariable(value = "concept_id") String concept_id,
                                                                   @PathVariable(value = "current_page")Integer current_page,
                                                                   @PathVariable(value = "size")Integer size) {
        return measurementService.getByConceptIDPage(concept_id, current_page, size);
    }

//    @GetMapping("/concept_name/{concept_name}")
//    public List<Measurement> searchMeasurementByConceptName(@PathVariable(value = "concept_name") String concept_name){
//        return measurementService.getByConceptName(concept_name);
//    }

    @GetMapping("/page/concept_name/{concept_name}")
    public IPage<Measurement> searchMeasurementByConceptNamePage(@PathVariable(value = "concept_name") String concept_name) {
        return measurementService.getByConceptNamePage(concept_name);
    }

    @GetMapping("/page/concept_name/{concept_name}/{current_page}/{size}")
    public IPage<Measurement> searchMeasurementByConceptNamePageWithSize(@PathVariable(value = "concept_name") String concept_name,
                                                                     @PathVariable(value = "current_page")Integer current_page,
                                                                     @PathVariable(value = "size")Integer size) {
        return measurementService.getByConceptNamePage(concept_name, current_page, size);
    }

//    @GetMapping("/concept_nctid/{concept_nctid}")
//    public List<Measurement> searchMeasurementByNCTID(@PathVariable(value = "concept_nctid") String concept_nctid){
//        return measurementService.getByNCTID(concept_nctid);
//    }

    @GetMapping("/page/concept_nctid/{concept_nctid}")
    public IPage<Measurement> searchMeasurementByNCTIDNamePage(@PathVariable(value = "concept_nctid") String concept_nctid) {
        return measurementService.getByNCTIDPage(concept_nctid);
    }

    @GetMapping("/page/concept_nctid/{concept_nctid}/{current_page}/{size}")
    public IPage<Measurement> searchMeasurementByNCTIDPageWithSize(@PathVariable(value = "concept_nctid") String concept_nctid,
                                                               @PathVariable(value = "current_page")Integer current_page,
                                                               @PathVariable(value = "size")Integer size) {
        return measurementService.getByNCTIDPage(concept_nctid, current_page, size);
    }


    @GetMapping("/values/concept_id/{concept_id}/{userMax}/{userMin}")
    public Map<String, Object> searchMeasurementValuesByConceptID(@PathVariable(value = "concept_id") String concept_id,
                                                                  @PathVariable(value = "userMax") float userMax,
                                                                  @PathVariable(value = "userMin") float userMin) {
        List<Measurement> values = measurementService.getAllMeasurementValuesByConceptId(concept_id);
        Map<String,Object> map=new HashMap<String,Object>();

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

            int numberBins = 250;
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
        return map;
    }
}

