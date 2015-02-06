(ns steam-tables.iapws95.coefficients
  (:require [clojure.edn :as edn]))

(def coefficients-file "resources\\iapws95-coefficients.edn")

(def coefficients (atom nil))

(defn initialize-coefficients!
  ([] (initialize-coefficients! coefficients-file))
  ([filename]
    (let [edn (slurp filename)
          state-edn (clojure.edn/read-string (str edn))]
      (reset! coefficients state-edn))))

(defn T-c [] (:T-c @coefficients))

(defn ρ-c [] (:rho-c @coefficients))

(defn R [] (:R @coefficients))

(defn retrieve-coefficient [t c]
  (fn [i]
    (c (first (filter #(= (:i %) i)
                    (t @coefficients))))))

(defn n-o [i]
  ((retrieve-coefficient :ideal-gas :n-o) i))

(defn γ-o [i]
  ((retrieve-coefficient :ideal-gas :gamma-o) i))

(defn c [i]
  ((retrieve-coefficient :residual :c) i))

(defn d [i]
  ((retrieve-coefficient :residual :d) i))

(defn t [i]
  ((retrieve-coefficient :residual :t) i))

(defn n [i]
  ((retrieve-coefficient :residual :n) i))

(defn α [i]
  ((retrieve-coefficient :residual :alpha) i))

(defn β [i]
  ((retrieve-coefficient :residual :beta) i))

(defn γ [i]
  ((retrieve-coefficient :residual :gamma) i))

(defn ε [i]
  ((retrieve-coefficient :residual :epsilon) i))

(defn a [i]
  ((retrieve-coefficient :residual :a) i))

(defn b [i]
  ((retrieve-coefficient :residual :b) i))

(defn B [i]
  ((retrieve-coefficient :residual :B) i))

(defn C [i]
  ((retrieve-coefficient :residual :C) i))

(defn D [i]
  ((retrieve-coefficient :residual :D) i))

(defn A [i]
  ((retrieve-coefficient :residual :A) i))
