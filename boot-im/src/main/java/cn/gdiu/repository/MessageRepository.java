package cn.gdiu.repository;

import cn.gdiu.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {


    @Query(value = "select * from t_message where (receiver = ?1 and sender = ?2) or (receiver = ?2 and sender = ?1)", nativeQuery = true)
    List<Message> findMessageHistoryList(Long myId, Long chatId);


}
