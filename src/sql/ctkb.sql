DROP TABLE IF EXISTS `criterion_ranking_table`;
-- # generate criterion ranking and save to a table
SET @rank= 0;
create table criterion_ranking_table
select aaa.concept_id, aaa.cri_count, @rank:=@rank + 1 AS rank_no
from (
	SELECT concept_id, count(DISTINCT nctid) as cri_count
	from ec_all_criteria
	where concept_id <> 'unmapped'
	GROUP BY concept_id
	ORDER BY cri_count
DESC) as aaa;