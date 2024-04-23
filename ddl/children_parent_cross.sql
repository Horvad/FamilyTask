CREATE TABLE IF NOT EXISTS app.children_parent_cross
(
    id_children bigint,
    id_parent bigint,
    CONSTRAINT children_parent_cross_id_children_fkey FOREIGN KEY (id_children)
        REFERENCES app.children (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT children_parent_cross_id_parent_fkey FOREIGN KEY (id_parent)
        REFERENCES app.parent (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)