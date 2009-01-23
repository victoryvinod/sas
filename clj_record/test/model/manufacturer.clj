(ns clj-record.test.model.manufacturer
  (:require clj-record.boot
            [clj-record.validation.built-ins :as vfn]))


; The following defs are to illustrate that validation messages and functions
; don't have to live inline in the init-model form.
(def my-grade-validation-message "negative!")
(defn my-grade-validation-fn [grade] (or (nil? grade) (>= grade 0)))

(defn infer-full-year [year]
  (if (= 2 (count year)) (str "19" year) year))

(clj-record.core/init-model
  (:associations
    (has-many products))
  (:validation
    (:name "empty!" #(not (empty? %)))
    (:name "starts with whitespace!" (vfn/non-match #"^\s"))
    (:name "ends with whitespace!" (vfn/non-match #"\s$"))
    (:founded "must be numeric" #(or (nil? %) (not (re-find #"\D" %))))
    (:grade my-grade-validation-message my-grade-validation-fn))
  (:callbacks
    (:before-save #(let [year (infer-full-year (% :founded))] (if year (assoc % :founded year) %)))))
