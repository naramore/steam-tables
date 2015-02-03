(ns steam-tables.iapws95.derivatives
    (:require [steam-tables.iapws95.coefficients :as coef]
              [steam-tables.iapws95.formula :only [Σ Δ θ ψ] :as formula])
    (:import java.lang.Math))

(defn ϕ-o-δ [δ]
  (/ 1 δ))

(defn ϕ-o-δδ [δ]
  (/ -1 (* δ δ)))

(defn ϕ-o-τ [δ τ]
  (+ (coef/n-o 2)
     (/ (coef/n-o 3) τ)
     (formula/Σ (partial f-o-τ δ τ) 4 8)))

(defn ϕ-o-ττ [δ τ]
  (+ (* (coef/n-o 3) (/ -1 (Math/pow τ 2)))
     (formula/Σ (partial f-o-ττ δ τ) 4 8)))

(defn ϕ-o-δτ [] 0)

(defn- f-o-τ [δ τ i]
  (* (coef/n-o i)
     (coef/γ-o i)
     (- (/ 1 (- 1 (Math/exp (* -1 (coef/γ-o i) τ)))) 1)))

(defn- f-o-ττ [δ τ i]
  (* (coef/n-o i)
     (Math/pow (coef/γ-o i) 2)
     (Math/exp (* -1 (coef/γ-o i) τ))
     (Math/pow (- 1 (Math/exp (* -1 (coef/γ-o i) τ))) -2)))


(defn ϕ-r-δ [δ τ]
  ())

(defn ϕ-r-δδ [δ τ]
  ())

(defn ϕ-r-δτ [δ τ]
  ())

(defn ϕ-r-τ [δ τ]
  ())

(defn ϕ-r-ττ [δ τ]
  ())



(defn- Δ-bi-δ [δ τ i]
  (* (coef/b i)
     (Math/pow (formula/Δ δ τ i) (- (coef/b i) 1))
     (Δ-δ δ τ i)))

(defn- Δ-bi-δδ [δ τ i]
  (* (coef/b i)
     (+ (* (Math/pow (formula/Δ δ τ i) (- (coef/b i) 1))
           (Δ-δδ δ τ i))
        (* (- (coef/b i) 1)
           (Math/pow (formula/Δ δ τ i) (- (coef/b i) 2))
           (Math/pow (Δ-δ δ τ i) 2)))))

(defn- Δ-bi-τ [δ τ i]
  (* -2
     (formula/θ δ τ i)
     (coef/b i)
     (Math/pow (formula/Δ δ τ i) (- (coef/b i) 1))))

(defn- Δ-bi-ττ [δ τ i]
  (+ (* 2
        (coef/b i)
        (Math/pow (formula/Δ δ τ i) (- (coef/b i) 1)))
     (* 4
        (Math/pow (formula/θ δ τ i) 2)
        (coef/b i)
        (- (coef/b i) 1)
        (Math/pow (formula/Δ δ τ i) (- (coef/b i) 2)))))

(defn- Δ-bi-δτ [δ τ i]
  ())

(defn- Δ-δ [δ τ i]
  ())

(defn- Δ-δδ [δ τ i]
  ())


(defn- ψ-δ [δ τ i]
  (* -2
     (coef/C i)
     (- δ 1)
     (formula/ψ δ τ i)))

(defn- ψ-δδ [δ τ i]
  (* (- (* 2 (coef/C i) (Math/pow (- δ 1) 2)) 1)
     2
     (coef/C i)
     (formula/ψ δ τ i)))

(defn- ψ-δτ [δ τ i]
  (* -2
     (coef/D i)
     (- τ 1)
     (formula/ψ δ τ i)))

(defn- ψ-τ [δ τ i]
  (* (- (* 2 (coef/D i) (Math/pow (- τ 1) 2)) 1)
     2
     (coef/D i)
     (formula/ψ δ τ i)))

(defn- ψ-ττ [δ τ i]
  (* 4
     (coef/C i)
     (coef/D i)
     (- δ 1)
     (- τ 1)
     (formula/ψ δ τ i)))
