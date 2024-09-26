import { useEffect } from "react";
import { useOrderContext } from "../../context/OrderContext";

const CartItemCount = ({ showModal, setShowModal }) => {
  const { cartQuantity } = useOrderContext();

  useEffect(() => {
    console.log(cartQuantity)
  }, [cartQuantity])

  return (
    <div className="nb-cart" onClick={() => setShowModal(!showModal)}>
      <div className="nb-cart-icon">
        <img src={"/Assets/cart.png"} alt="Cart" className="nb-image-cart" />
      </div>
      <div className="nb-cart-count">
        <span>{cartQuantity}</span>
      </div>
    </div>
  );
};

export default CartItemCount;
