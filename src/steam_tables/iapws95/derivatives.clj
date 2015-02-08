(ns steam-tables.iapws95.derivatives
    (:require [steam-tables.iapws95.coefficients :as coef]
              [steam-tables.iapws95.formula :only [Σ Δ θ ψ] :as formula])
    (:import java.lang.Math))

(defn ϕ-o-δ [δ τ]
  (/ 1 δ))

(defn ϕ-o-δδ [δ τ]
  (/ -1 (* δ δ)))

(defn ϕ-o-τ [δ τ]
  (+ (coef/n-o 2)
     (/ (coef/n-o 3) τ)
     (formula/Σ (partial f-o-τ δ τ) 4 8)))

(defn ϕ-o-ττ [δ τ]
  (- (* -1 (coef/n-o 3)
        (Math/pow τ -2))
     (formula/Σ (partial f-o-ττ δ τ) 4 8)))

(defn ϕ-o-δτ [δ τ] 0)

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
  (let [f-1 #(* (coef/n %)
                (coef/d %)
                (Math/pow δ (- (coef/d %) 1))
                (Math/pow τ (coef/t %)))
        f-2 #(* (coef/n %)
                (Math/exp (* -1 (Math/pow δ (coef/c %))))
                (* (Math/pow δ (- (coef/d %) 1))
                   (Math/pow τ (coef/t %))
                   (- (coef/d %)
                      (* (coef/c %)
                         (Math/pow δ (coef/c %))))))
        f-3 #(* (coef/n %)
                (Math/pow δ (coef/d %))
                (Math/pow τ (coef/t %))
                (Math/exp (- (* -1 (coef/α %)
                                (Math/pow (- δ (coef/ε %)) 2))
                             (* (coef/β %)
                                (Math/pow (- τ (coef/γ %)) 2))))
                (- (/ (coef/d %) δ)
                   (* 2
                      (coef/α %)
                      (- δ (coef/ε %)))))
        f-4 #(* (coef/n %)
                (+ (* (Math/pow (formula/Δ δ τ %) (coef/b %))
                      (+ (formula/ψ δ τ %)
                         (* δ (ψ-δ δ τ %))))
                   (* (Δ-bi-δ δ τ %)
                      δ
                      (formula/ψ δ τ %))))]
    (+ (formula/Σ f-1 1 7)
       (formula/Σ f-2 8 51)
       (formula/Σ f-3 52 54)
       (formula/Σ f-4 55 56))))

(defn ϕ-r-δδ [δ τ]
  (let [f-1 #(* (coef/n %)
                (coef/d %)
                (Math/pow δ (- (coef/d %) 1))
                (Math/pow τ (coef/t %)))
        f-2 #(* (coef/n %)
                (Math/exp (* -1 (Math/pow δ (coef/c %))))
                (Math/pow δ (- (coef/d %) 2))
                (Math/pow τ (coef/t %))
                (- (* (- (coef/d %)
                         (* (coef/c %)
                            (Math/pow δ (coef/c %))))
                      (- (coef/d %)
                         1
                         (* (coef/c %)
                            (Math/pow δ (coef/c %)))))
                   (* (Math/pow (coef/c %) 2)
                      (Math/pow δ (coef/c %)))))
        f-3 #(* (coef/n %)
                (Math/pow τ (coef/t %))
                (Math/exp (- (* -1 (coef/α %)
                                (Math/pow (- δ (coef/ε %)) 2))
                             (* (coef/β %)
                                (Math/pow (- τ (coef/γ %)) 2))))
                (+ (* -2
                      (coef/α %)
                      (Math/pow δ (coef/d %)))
                   (* 4
                      (Math/pow (coef/α %) 2)
                      (Math/pow δ (coef/d %))
                      (Math/pow (- δ (coef/ε %)) 2))
                   (* -4
                      (coef/d %)
                      (coef/α %)
                      (Math/pow δ (- (coef/d %) 1))
                      (- δ (coef/ε %)))
                   (* (coef/d %)
                      (- (coef/d %) 1)
                      (Math/pow δ (- (coef/d %) 2)))))
        f-4 #(* (coef/n %)
                (+ (* (Math/pow (formula/Δ δ τ %) (coef/b %))
                      (+ (* 2 (ψ-δ δ τ %))
                         (* δ (ψ-δδ δ τ %))))
                   (* 2
                      (Δ-bi-δ δ τ %)
                      (+ (formula/ψ δ τ %)
                         (* δ (ψ-δ δ τ %))))
                   (* (Δ-bi-δδ δ τ %)
                      δ
                      (formula/ψ δ τ %))))]
    (+ (formula/Σ f-1 1 7)
       (formula/Σ f-2 8 51)
       (formula/Σ f-3 52 54)
       (formula/Σ f-4 55 56))))

(defn ϕ-r-τ [δ τ]
  (let [f-1 #(* (coef/n %)
                (coef/t %)
                (Math/pow δ (coef/d %))
                (Math/pow τ (- (coef/t %) 1)))
        f-2 #(* (f-1 %)
                (Math/exp (* -1 (Math/pow δ (coef/c %)))))
        f-3 #(* (coef/n %)
                (Math/pow δ (coef/d %))
                (Math/pow τ (coef/t %))
                (Math/exp (- (* -1 (coef/α %)
                                (Math/pow (- δ (coef/ε %)) 2))
                             (* (coef/β %)
                                (Math/pow (- τ (coef/γ %)) 2))))
                (- (/ (coef/t %) τ)
                   (* 2 (coef/β %)
                      (- τ (coef/γ %)))))
        f-4 #(* (coef/n %) δ
                (+ (* (Δ-bi-τ δ τ %)
                      (formula/ψ δ τ %))
                   (* (Math/pow (formula/Δ δ τ %) (coef/b %))
                      (ψ-τ δ τ %))))]
    (+ (formula/Σ f-1 1 7)
       (formula/Σ f-2 8 51)
       (formula/Σ f-3 52 54)
       (formula/Σ f-4 55 56))))

(defn ϕ-r-ττ [δ τ]
  (let [f-1 #(* (coef/n %)
                (coef/t %)
                (- (coef/t %) 1)
                (Math/pow δ (coef/d %))
                (Math/pow τ (- (coef/t %) 2)))
        f-2 #(* (f-1 %)
                (Math/exp (* -1 (Math/pow δ (coef/c %)))))
        f-3 #(* (coef/n %)
                (Math/pow δ (coef/d %))
                (Math/pow τ (coef/t %))
                (Math/exp (- (* -1 (coef/α %)
                                (Math/pow (- δ (coef/ε %)) 2))
                             (* (coef/β %)
                                (Math/pow (- τ (coef/γ %)) 2))))
                (- (Math/pow (- (/ (coef/t %) τ)
                                (* 2 (coef/β %)
                                     (- τ (coef/γ %)))) 2)
                   (/ (coef/t %) (Math/pow τ 2))
                   (* 2 (coef/β %))))
        f-4 #(* (coef/n %) δ
                (+ (* (Δ-bi-ττ δ τ %)
                      (formula/ψ δ τ %))
                   (* 2
                      (Δ-bi-τ δ τ %)
                      (ψ-τ δ τ %))
                   (* (Math/pow (formula/Δ δ τ %) (coef/b %))
                      (ψ-ττ δ τ %))))]
    (+ (formula/Σ f-1 1 7)
       (formula/Σ f-2 8 51)
       (formula/Σ f-3 52 54)
       (formula/Σ f-4 55 56))))

(defn ϕ-r-δτ [δ τ]
  (let [f-1 #(* (coef/n %)
                (coef/d %)
                (coef/t %)
                (Math/pow δ (- (coef/d %) 1))
                (Math/pow τ (- (coef/t %) 1)))
        f-2 #(* (coef/n %)
                (coef/t %)
                (Math/pow δ (- (coef/d %) 1))
                (Math/pow τ (- (coef/t %) 1))
                (- (coef/d %)
                   (* (coef/c %)
                      (Math/pow δ (coef/c %))))
                (Math/exp (* -1 (Math/pow δ (coef/c %)))))
        f-3 #(* (coef/n %)
                (Math/pow δ (coef/d %))
                (Math/pow τ (coef/t %))
                (Math/exp (- (* -1 (coef/α %)
                                (Math/pow (- δ (coef/ε %)) 2))
                             (* (coef/β %)
                                (Math/pow (- τ (coef/γ %)) 2))))
                (- (/ (coef/d %) δ)
                   (* -2
                      (coef/α %)
                      (- δ (coef/ε %))))
                (- (/ (coef/t %) τ)
                   (* -2
                      (coef/β %)
                      (- τ (coef/γ %)))))
        f-4 #(* (coef/n %)
                (+ (* (Math/pow (formula/Δ δ τ %) (coef/b %))
                      (+ (ψ-τ δ τ %)
                         (* δ (ψ-δτ δ τ %))))
                   (* δ
                      (Δ-bi-δ δ τ %)
                      (ψ-τ δ τ %))
                   (* (Δ-bi-τ δ τ %)
                      (+ (formula/ψ δ τ %)
                         (* δ (ψ-δ δ τ %))))
                   (* (Δ-bi-δτ δ τ %) δ
                      (formula/ψ δ τ %))))]
    (+ (formula/Σ f-1 1 7)
       (formula/Σ f-2 8 51)
       (formula/Σ f-3 52 54)
       (formula/Σ f-4 55 56))))


(defn- Δ-bi-δ [δ τ i]
  (* (coef/b i)
     (Math/pow (formula/Δ δ τ i) (- (coef/b i) 1))
     (Δ-δ δ τ i)))

(defn- Δ-bi-δδ [δ τ i]
  (* (coef/b i)
     (+ (* (Math/pow (formula/Δ δ τ i)
                     (- (coef/b i) 1))
           (Δ-δδ δ τ i))
        (* (- (coef/b i) 1)
           (Math/pow (formula/Δ δ τ i)
                     (- (coef/b i) 2))
           (Math/pow (Δ-δ δ τ i) 2)))))

(defn- Δ-bi-τ [δ τ i]
  (* -2
     (formula/θ δ τ i)
     (coef/b i)
     (Math/pow (formula/Δ δ τ i) (- (coef/b i) 1))))

(defn- Δ-bi-ττ [δ τ i]
  (+ (* 2
        (coef/b i)
        (Math/pow (formula/Δ δ τ i)
                  (- (coef/b i) 1)))
     (* 4
        (Math/pow (formula/θ δ τ i) 2)
        (coef/b i)
        (- (coef/b i) 1)
        (Math/pow (formula/Δ δ τ i)
                  (- (coef/b i) 2)))))

(defn- Δ-bi-δτ [δ τ i]
  (- (* -1 (coef/A i)
        (coef/b i)
        (/ 2 (coef/β i))
        (Math/pow (formula/Δ δ τ i)
                  (- (coef/b i) 1))
        (- δ 1)
        (Math/pow (Math/pow (- δ 1) 2)
                  (- (/ 1 (* 2 (coef/β i))) 1)))
     (* 2
        (formula/θ δ τ i)
        (coef/b i)
        (- (coef/b i) 1)
        (Math/pow (formula/Δ δ τ i)
                  (- (coef/b i) 2))
        (Δ-δ δ τ i))))

(defn- Δ-δ [δ τ i]
  (* (- δ 1)
     (+ (* (coef/A i)
           (formula/θ δ τ i)
           (/ 2 (coef/β i))
           (Math/pow (Math/pow (- δ 1) 2)
                     (- (/ 1 (* 2 (coef/β i))) 1)))
        (* 2
           (coef/B i)
           (coef/a i)
           (Math/pow (Math/pow (- δ 1) 2)
                     (- (coef/a i) 1))))))

(defn- Δ-δδ [δ τ i]
  (+ (/ (Δ-δ δ τ i)
        (- δ 1))
     (* (Math/pow (- δ 1) 2)
        (+ (* 4
              (coef/B i)
              (coef/a i)
              (- (coef/a i) 1)
              (Math/pow (Math/pow (- δ 1) 2) (- (coef/a i) 2)))
           (* 2
              (Math/pow (coef/A i) 2)
              (Math/pow (coef/β i) -2)
              (Math/pow (Math/pow (Math/pow (- δ 1) 2)
                                  (- (/ 1 (* 2 (coef/β i))) 1)) 2))
           (* (coef/A i)
              (formula/θ δ τ i)
              (/ 4 (coef/β i))
              (- (/ 1 (* 2 (coef/β i))) 1)
              (Math/pow (Math/pow (- δ 1) 2)
                        (- (/ 1 (* 2 (coef/β i))) 2)))))))


(defn- ψ-δ [δ τ i]
  (* -2
     (coef/C i)
     (- δ 1)
     (formula/ψ δ τ i)))

(defn- ψ-δδ [δ τ i]
  (* (- (* 2
           (coef/C i)
           (Math/pow (- δ 1) 2)) 1)
     2
     (coef/C i)
     (formula/ψ δ τ i)))

(defn- ψ-τ [δ τ i]
  (* -2
     (coef/D i)
     (- τ 1)
     (formula/ψ δ τ i)))

(defn- ψ-ττ [δ τ i]
  (* (- (* 2
           (coef/D i)
           (Math/pow (- τ 1) 2)) 1)
     2
     (coef/D i)
     (formula/ψ δ τ i)))

(defn- ψ-δτ [δ τ i]
  (* 4
     (coef/C i)
     (coef/D i)
     (- δ 1)
     (- τ 1)
     (formula/ψ δ τ i)))
