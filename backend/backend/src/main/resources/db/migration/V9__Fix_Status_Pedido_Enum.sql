UPDATE pedidos SET status = 'AGUARDANDO_PAGAMENTO' WHERE status = 'Pagamento_em_Aguardo';
UPDATE pedidos SET status = 'PAGO'               WHERE status = 'Pago';
UPDATE pedidos SET status = 'PAGAMENTO_RECUSADO' WHERE status = 'Pagamento_Recusado';
UPDATE pedidos SET status = 'EM_PREPARO'         WHERE status = 'Em_Preparo';
UPDATE pedidos SET status = 'PRONTO'             WHERE status = 'Pronto';
UPDATE pedidos SET status = 'ENTREGUE'           WHERE status = 'Entregue';
UPDATE pedidos SET status = 'CANCELADO'          WHERE status = 'Cancelado';
