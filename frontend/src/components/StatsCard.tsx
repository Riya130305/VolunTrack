import React from 'react';

interface StatsCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
}

const StatsCard: React.FC<StatsCardProps> = ({ title, value, subtitle }) => (
  <div className="bg-white p-5 rounded-xl shadow-sm border border-gray-200">
    <div className="text-sm font-medium text-gray-500">{title}</div>
    <div className="mt-3 text-3xl font-semibold text-gray-900">{value}</div>
    {subtitle && <div className="mt-1 text-sm text-gray-500">{subtitle}</div>}
  </div>
);

export default StatsCard;
