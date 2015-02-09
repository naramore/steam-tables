(ns steam-tables.iapws95-test
  (:require [clojure.test :refer :all]
            [steam-tables.iapws95.properties :as properties :refer :all]
            [steam-tables.iapws95.formula :as formula :refer :all]
            [steam-tables.iapws95.derivatives :as derivatives :refer :all]
            [steam-tables.iapws95.coefficients :as coefficients :refer :all]))

;; The following tests are taken from:  http://www.iapws.org/relguide/IAPWS95-2014.pdf
;; Table 6, Table 7, and Table 8

;; Table 6. Values for the ideal-gas part ϕ-o, Eq. (5), and for the residual part ϕ-r,
;;          Eq. (6), of the dimensionless Helmholtz free energy together with the
;;          corresponding derivativesa for T = 500 K and ρ = 838.025 kg/m^3
;;
;; For the abbreviated notation of the derivatives of ϕ-o and ϕ-r see the footnotes of Tables 4 and
;; 5, respectively.
(def table-6 {:T         500              :rho       838.025
              :phi-o     0.204797733E+1   :phi-r    -0.342693206E+1
              :phi-o-d   0.384236747E+0   :phi-r-d  -0.364366650E+0
              :phi-o-dd -0.147637878E+0   :phi-r-dd  0.856063701E+0
              :phi-o-t   0.904611106E+1   :phi-r-t  -0.581403435E+1
              :phi-o-tt -0.193249185E+1   :phi-r-tt -0.223440737E+1
              :phi-o-dt  0.0              :phi-r-dt -0.112176915E+1})

;; This is a map of keys to functions from the table-6 map
(def table-6-functions {:phi-o    formula/ϕ-o          :phi-r    formula/ϕ-r
                        :phi-o-d  derivatives/ϕ-o-δ    :phi-r-d  derivatives/ϕ-r-δ
                        :phi-o-dd derivatives/ϕ-o-δδ   :phi-r-dd derivatives/ϕ-r-δδ
                        :phi-o-t  derivatives/ϕ-o-τ    :phi-r-t  derivatives/ϕ-r-τ
                        :phi-o-tt derivatives/ϕ-o-ττ   :phi-r-tt derivatives/ϕ-r-ττ
                        :phi-o-dt derivatives/ϕ-o-δτ   :phi-r-dt derivatives/ϕ-r-δτ})

;; Table 7. Thermodynamic property values in the single-phase region for selected values of T and ρ
;;
;; In the liquid-water region at low pressures small changes in density along an isotherm cause large changes in pressure.
;; For this reason, due to an accumulation of small errors, a particular computer code or a particular PC may fail to
;; reproduce the pressure value with nine significant figures.
(def table-7 [{ :T 300  :rho 0.9965560E+3  :p 0.992418352E-1  :c-v 0.413018112E+1  :w 0.150151914E+4  :s 0.393062643E+0 }
              { :T 300  :rho 0.1005308E+4  :p 0.200022515E+2  :c-v 0.406798347E+1  :w 0.153492501E+4  :s 0.387405401E+0 }
              { :T 300  :rho 0.1188202E+4  :p 0.700004704E+3  :c-v 0.346135580E+1  :w 0.244357992E+4  :s 0.132609616E+0 }
              { :T 500  :rho 0.4350000E+0  :p 0.999679423E-1  :c-v 0.150817541E+1  :w 0.548314253E+3  :s 0.794488271E+1 }
              { :T 500  :rho 0.4532000E+1  :p 0.999938125E+0  :c-v 0.166991025E+1  :w 0.535739001E+3  :s 0.682502725E+1 }
              { :T 500  :rho 0.8380250E+3  :p 0.100003858E+2  :c-v 0.322106219E+1  :w 0.127128441E+4  :s 0.256690919E+1 }
              { :T 500  :rho 0.1084564E+4  :p 0.700000405E+3  :c-v 0.307437693E+1  :w 0.241200877E+4  :s 0.203237509E+1 }
              { :T 647  :rho 0.3580000E+3  :p 0.220384756E+2  :c-v 0.618315728E+1  :w 0.252145078E+3  :s 0.432092307E+1 }
              { :T 900  :rho 0.2410000E+0  :p 0.100062559E+0  :c-v 0.175890657E+1  :w 0.724027147E+3  :s 0.916653194E+1 }
              { :T 900  :rho 0.5261500E+2  :p 0.200000690E+2  :c-v 0.193510526E+1  :w 0.698445674E+3  :s 0.659070225E+1 }
              { :T 900  :rho 0.8707690E+3  :p 0.700000006E+3  :c-v 0.266422350E+1  :w 0.201933608E+4  :s 0.417223802E+1 }])

;; This is a mpa of keys to functions from the table-7 map
(def table-7-functions {:p   #(* (properties/p %1 %2)
                                 1.0E-3) ; kPa --> MPa conversion
                        :c-v properties/c-v
                        :w   #(* (properties/w %1 %2)
                                 (Math/sqrt 1000))
                        :s   properties/s})

;; Table 8. Thermodynamic property values in the two-phase region for selected values of
;;          temperature
;;
;; All these test values were calculated from the Helmholtz free energy, Eq. (4), by applying the phase-
;; equilibrium condition (Maxwell criterion).
(def table-8 [{:T       275
               :p-s     0.698451167E-3
               :rho-f   0.999887406E+3
               :rho-g   0.550664919E-2
               :h-f     0.775972202E+1
               :h-g     0.250428995E+4
               :s-f     0.283094670E-1
               :s-g     0.910660121E+1}
              {:T       450
               :p-s     0.932203564E+0
               :rho-f   0.890341250E+3
               :rho-g   0.481200360E+1
               :h-f     0.749161585E+3
               :h-g     0.277441078E+4
               :s-f     0.210865845E+1
               :s-g     0.660921221E+1}
              {:T       625
               :p-s     0.169082693E+2
               :rho-f   0.567090385E+3
               :rho-g   0.118290280E+3
               :h-f     0.168626976E+4
               :h-g     0.255071625E+4
               :s-f     0.380194683E+1
               :s-g     0.518506121E+1}])

(defn scientific-compare [x y p]
  "Compares two values, x & y, by formatting them to scientific notation with precision, p"
  (let [f #(format (str "%." p "e") %)]
    (= (f x) (f y))))

(defn test-table-7-property [k p]
  "Tests the property, selected by the keyword k, at the precision, p"
  (every? #(= % true)
          (map #(scientific-compare (k %)
                                    ((k table-7-functions) (:rho %)
                                                           (:T %)) p) table-7)))

(defn table-7-compare [k p]
  (let [my-compare #(scientific-compare (k %)
                                        ((k table-7-functions) (:rho %)
                                                               (:T %)) p)
        my-temp #(:T %)
        my-rho #(:rho %)]
    (map #(hash-map :T (my-temp %)
                    :rho (my-rho %)
                    :same (my-compare %)) table-7)))

(defn table-7-test [k p]
  (map #(testing (str "@ T = " (:T %) " K, & rho = " (:rho %) "kg/m^3") (is (:same %)))
       (testing-compare k p)))

;; Test for the ideal-gas and residual parts of the dimensionless Helmholtz
;; free energy equation as compared to Table 6. values
(deftest helmholtz-free-energy-parts-test
  (do
    (coefficients/initialize-coefficients!)
    (let [delta (properties/δ (:rho table-6))
          tau (properties/τ (:T table-6))
          my-compare #(scientific-compare (% table-6)
                                          ((% table-6-functions) delta tau) 8)]
      (testing "the dimensionless Helmholtz free energy: "
        (testing "ideal-gas part --"
          (testing "normal function"
            (is (my-compare :phi-o)))
          (testing "1st derivative wrt δ"
            (is (my-compare :phi-o-d)))
          (testing "2nd derivative wrt δ & δ"
            (is (my-compare :phi-o-dd)))
          (testing "1st derivative wrt τ"
            (is (my-compare :phi-o-t)))
          (testing "2nd derivative wrt τ & τ"
            (is (my-compare :phi-o-tt)))
          (testing "2nd derivative wrt δ & τ"
            (is (my-compare :phi-o-dt))))
        (testing "residual part --"
          (testing "normal function"
            (is (my-compare :phi-r)))
          (testing "1st derivative wrt δ"
            (is (my-compare :phi-r-d)))
          (testing "2nd derivative wrt δ & δ"
            (is (my-compare :phi-r-dd)))
          (testing "1st derivative wrt τ"
            (is (my-compare :phi-r-t)))
          (testing "2nd derivative wrt τ & τ"
            (is (my-compare :phi-r-tt)))
          (testing "2nd derivative wrt δ & τ"
            (is (my-compare :phi-r-dt))))))))

;; Test that the single-phase thermodynamic properties line up with
;; the Table 7. values
(deftest single-phase-thermodynamic-property-test
  (do
    (coefficients/initialize-coefficients!)
    (testing "the single-phase thermodynamic property:"
      (testing "pressure, p [MPa] --> "
        (table-7-test :p 8))
      (testing "isochoric heat capacity, c-v [kJ/kg-K] --> "
        (table-7-test :c-v 8))
      (testing "speed of sound, w [m/s] --> "
        (table-7-test :w 8))
      (testing "entropy, s [kJ/kg-K] --> "
        (table-7-test :s 8)))))

;; Test that the two-phase thermodynamic properties line up with
;; the Table 8. values
(deftest two-phase-thermodynamic-property-test
  (testing "todo"
    (is (= true true))))
