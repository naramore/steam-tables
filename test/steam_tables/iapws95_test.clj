(ns steam-tables.iapws95-test
  (:require [clojure.test :refer :all]
            [steam-tables.iapws95.properties :as properties :refer :all]
            [steam-tables.iapws95.formula :as formula :refer :all]
            [steam-tables.iapws95.derivatives :as derivatives :refer :all]
            [steam-tables.iapws95.coefficients :as coefficients :refer :all]))

(def table-6 {:T         500              :rho       838.025
              :phi-o     0.204797733E+1   :phi-r    -0.342693206E+1
              :phi-o-d   0.384236747E+0   :phi-r-d  -0.364366650E+0
              :phi-o-dd -0.147637878E+0   :phi-r-dd  0.856063701E+0
              :phi-o-t   0.904611106E+1   :phi-r-t  -0.581403435E+1
              :phi-o-tt -0.193249185E+1   :phi-r-tt -0.223440737E+1
              :phi-o-dt  0                :phi-r-dt -0.112176915E+1})

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

(deftest helmholtz-free-energy-parts-test
  (testing "that the ideal-gas and residual parts of the dimensionless Helmholtz
            free energy and their derivatives correspond to the table-6 values"
    (let [delta (properties/δ (:rho table-6))
          tau (properties/τ (:T table-6))]
      (do
        (coefficients/initialize-coefficients!)
        (is (= table-6
               {:T        500                              :rho      838.025
                :phi-o    (formula/ϕ-o delta tau)          :phi-r    (formula/ϕ-r delta tau)
                :phi-o-d  (derivatives/ϕ-o-δ delta tau)    :phi-r-d  (derivatives/ϕ-r-δ delta tau)
                :phi-o-dd (derivatives/ϕ-o-δδ delta tau)   :phi-r-dd (derivatives/ϕ-r-δδ delta tau)
                :phi-o-t  (derivatives/ϕ-o-τ delta tau)    :phi-r-t  (derivatives/ϕ-r-τ delta tau)
                :phi-o-tt (derivatives/ϕ-o-ττ delta tau)   :phi-r-tt (derivatives/ϕ-r-ττ delta tau)
                :phi-o-dt (derivatives/ϕ-o-δτ delta tau)   :phi-r-dt (derivatives/ϕ-r-δτ delta tau)}))))))

(deftest single-phase-thermodynamic-property-test
  (testing "that the pressure [MPa], isochoric heat capacity [kJ/kg-K], speed of
            sound [m/s], and entropy [kJ/kg-K] in the single-phase region
            correspond to the table-7 values"
    (is (= true true))))

(deftest two-phase-thermodynamic-property-test
  (testing "todo"
    (is (= true true))))
