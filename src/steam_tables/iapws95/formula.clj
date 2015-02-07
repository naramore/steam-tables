(ns steam-tables.iapws95.formula
    (:require [steam-tables.iapws95.coefficients :as coef])
    (:import java.lang.Math))

(defn ϕ-o [δ τ]
    "Represents the ideal-gas part of the dimensionless Helmhotlz free enegery (Eq. 5, IAPWS95-2014.pdf)"
    (+ (Math/log δ)
       (coef/n-o 1)
       (* (coef/n-o 2) τ)
       (* (coef/n-o 3) (Math/log τ))
       (Σ (partial f-o τ) 4 8)))

(defn- f-o [τ i]
    (* (coef/n-o i)
       (Math/log (- 1 (Math/exp (* -1 (coef/γ-o i) τ))))))

(defn ϕ-r [δ τ]
    (+ (Σ (partial f-r-1 δ τ) 1 7)
       (Σ (partial f-r-2 δ τ) 8 51)
       (Σ (partial f-r-3 δ τ) 52 54)
       (Σ (partial f-r-4 δ τ) 55 56)))

(defn- f-r-1 [δ τ i]
    (* (coef/n i)
       (Math/pow δ (coef/d i))
       (Math/pow τ (coef/t i))))

(defn- f-r-2 [δ τ i]
    (* (f-r-1 δ τ i)
       (Math/exp (* -1 (Math/exp (* δ (coef/c i)))))))

(defn- f-r-3 [δ τ i]
    (* (f-r-1 δ τ i)
       (Math/exp (- (* -1 (coef/α i)
                       (Math/pow (- δ (coef/ε i)) 2))
                    (* (coef/β i)
                       (Math/pow (- τ (coef/γ i)) 2))))))

(defn- f-r-4 [δ τ i]
    (* (coef/n i)
       (Math/pow (Δ δ τ i) (coef/b i))
       δ
       (ψ δ τ i)))

(defn Δ [δ τ i]
    (+ (Math/pow (θ δ τ i) 2)
       (* (coef/B i)
          (Math/pow (Math/pow (- δ 1) 2)
                    (coef/a i)))))

(defn θ [δ τ i]
    (+ (- 1 τ)
       (* (coef/A i)
          (Math/pow (Math/pow (- δ 1) 2)
                    (/ 1 (* 2 (coef/β i)))))))

(defn ψ [δ τ i]
    (Math/exp (- (* -1 (coef/C i)
                   (Math/pow (- δ 1) 2))
                 (* (coef/D i)
                    (Math/pow (- τ 1) 2)))))

(defn Σ [f i n]
    (reduce + (map f (range i (+ n 1)))))
