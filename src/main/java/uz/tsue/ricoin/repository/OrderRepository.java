package uz.tsue.ricoin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tsue.ricoin.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
