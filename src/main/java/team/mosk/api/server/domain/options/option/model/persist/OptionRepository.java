package team.mosk.api.server.domain.options.option.model.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import team.mosk.api.server.domain.options.option.model.persist.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
