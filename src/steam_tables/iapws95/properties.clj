(ns steam-tables.iapws95.properties
    (:require [steam-tables.iapws95.formula :only [ϕ-o ϕ-r] :as f]
              [steam-tables.iapws95.derivatives :as d]
              [steam-tables.iapws95.coefficients :as c])
    (:import java.lang.Math))

(defn δ [ρ] (/ ρ (c/ρ-c)))

(defn τ [T] (/ (c/T-c) T))


(defn p [ρ T]
  (* ρ T (c/R)
     (+ 1 (* (δ ρ) (d/ϕ-r-δ (δ ρ) (τ T))))))

(defn u [ρ T]
  (* (c/R) (c/T-c)
     (+ (d/ϕ-o-τ (δ ρ) (τ T))
        (d/ϕ-r-τ (δ ρ) (τ T)))))

(defn s [ρ T]
  (* (c/R)
     (- (* (τ T) (+ (d/ϕ-o-τ (δ ρ) (τ T))
                    (d/ϕ-r-τ (δ ρ) (τ T))))
        (f/ϕ-o (δ ρ) (τ T))
        (f/ϕ-r (δ ρ) (τ T)))))

(defn h [ρ T]
  (+ (* (c/R) T)
     (u ρ T)
     (* (δ ρ)
        (d/ϕ-r-δ (δ ρ) (τ T)))))

(defn c-v [ρ T]
  (* -1 (c/R)
     (Math/pow (τ T) 2)
     (+ (d/ϕ-o-ττ (δ ρ) (τ T))
        (d/ϕ-r-ττ (δ ρ) (τ T)))))

(defn c-p [ρ T]
  (+ (c-v ρ T)
     (/ (* (c/R) (Math/pow (+ 1 (* (δ ρ) (d/ϕ-r-δ (δ ρ) (τ T)))
                                (* -1 (δ ρ) (τ T) (d/ϕ-r-δτ (δ ρ) (τ T)))) 2))
        (+ 1 (* 2 (δ ρ) (d/ϕ-r-δ (δ ρ) (τ T)))
             (* (Math/pow (δ ρ) 2) (d/ϕ-r-δδ (δ ρ) (τ T)))))))

(defn w [ρ T]
  (* (c/R) T
     (+ 1 (* 2 (δ ρ) (d/ϕ-r-δ (δ ρ) (τ T)))
          (* (Math/pow (δ ρ) 2) (d/ϕ-r-δδ (δ ρ) (τ T)))
          (/ (Math/pow (+ 1 (* (δ ρ) (d/ϕ-r-δ (δ ρ) (τ T)))
                            (* -1 (δ ρ) (τ T) (d/ϕ-r-δτ (δ ρ) (τ T)))) 2)
             (c-v ρ T)))))
